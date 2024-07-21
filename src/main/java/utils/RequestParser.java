package utils;

import constant.RequestHeader;
import enums.HttpMethod;
import enums.MimeType;
import model.Part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    private static final Logger logger = LoggerFactory.getLogger(RequestParser.class);

    private static class LazyRequestParser {
        public static RequestParser instance = new RequestParser();
    }

    public static RequestParser getRequestParser() {
        return LazyRequestParser.instance;
    }

    private RequestParser() {}

    /**
     * raw request message를 파싱하는 메서드
     * @param rawHttpRequest
     * @return
     * @throws IOException
     */
    public Request getRequest(InputStream rawHttpRequest) throws IOException {
        ByteLineReader br = new ByteLineReader(rawHttpRequest);

        Map<String, Part> parts = null;

        // request line parse
        String[] requestLines = requestLineParse(br.readLine());
        HttpMethod method = HttpMethod.from(requestLines[0]);
        Map<String, String> queryParameters = pathParse(requestLines[1]);
        String path = queryParameters.get("path");
        String httpVersion = requestLines[2];

        // request header parse
        Map<String, String> headers = requestHeaderParse(br);


        // request body parse
        String body = requestBodyParse(headers.get(RequestHeader.CONTENT_LENGTH), br);

        String contentType = headers.getOrDefault(RequestHeader.CONTENT_TYPE, MimeType.OCTET.toString());
        String[] contentTypeOptions = contentType.split(";");
        Map<String, String> options = new HashMap<>();
        contentType = contentTypeOptions[0].trim();

        for(int i = 1; i < contentTypeOptions.length; i++) {
            String[] typeOptionkeyValue = contentTypeOptions[i].split("=");
            if(typeOptionkeyValue.length != 2) {
                break;
            }
            String key = typeOptionkeyValue[0].trim().toLowerCase();
            String value = typeOptionkeyValue[1].trim();
            options.put(key, value);
        }

        if(contentType.equals(MimeType.FORM.getMimeType())) {
            Map<String, String> bodyQuerys = new HashMap<>();
            if (body != null) {
                bodyQuerys = queryParse(body, "&", "=");
            }
            queryParameters.putAll(bodyQuerys);
        } else if(contentType.equals(MimeType.MULTIPART_FORM.toString())) {
            String boundary = options.get("boundary");
            parts = multipartFormParse(body, boundary);
        }
        // cookie parse
        Map<String, String> cookies;
        String rawCookie = headers.get("cookie");
        if(rawCookie != null) {
            cookies = queryParse(rawCookie, ";", "=");
        } else {
            cookies = new HashMap<>();
        }

        return new Request.RequestBuilder()
                .setMethod(method)
                .setPath(path)
                .setHttpVersion(httpVersion)
                .setHttpHeaders(headers)
                .setParameters(queryParameters)
                .setCookies(cookies)
                .setBody(body)
                .setParts(parts)
                .build();
    }

    /**
     * 멀티 파트 전체 메시지를 파싱하는 메서드
     * @param body
     * @param boundary
     * @return
     */
    private Map<String, Part> multipartFormParse(String body, String boundary) {
        String[] formData = body.split("--" + boundary);
        Map<String, Part> parts = new HashMap<>();
        for(int i = 1; i < formData.length; i++) {
            formDataParse(formData[i], parts);
            logger.debug("formLine = {}", formData[i]);
        }
        return parts;
    }

    /**
     * 한개의 폼데이터를 파싱하는 메서드
     * @param formData
     * @param parts
     */
    private void formDataParse(String formData, Map<String, Part> parts) {
        String[] formLines = formData.split("\r\n");
        int i = Math.min(1, formLines.length - 1);
        String dispositionType = null;
        String name = null;
        String filename = null;
        MimeType contentType = MimeType.PLAIN;
        while(i < formLines.length && !formLines[i].isEmpty()) {
            String formLine = formLines[i];
            String[] formKeyValue = formLine.split(":");
            if(formKeyValue.length != 2) {
                i++;
                continue;
            }
            String key = formKeyValue[0].toLowerCase().trim();
            String value = formKeyValue[1];

            if(key.equals(Part.CONTENT_DISPOSITION)) {
                String[] dispositionKeyVals = value.split(";");
                dispositionType = dispositionKeyVals[0].trim();
                for(int di = 1; di < dispositionKeyVals.length; di++) {
                    String[] paramKeyVals = dispositionKeyVals[di].split("=");
                    if(paramKeyVals.length != 2) continue;
                    String paramKey = paramKeyVals[0].trim();
                    String paramValue = paramKeyVals[1].trim().replaceAll("\"", "");
                    logger.debug("key = {} value = {}", paramKey, paramValue);
                    if(paramKey.equals("name")) {
                        name = paramValue;
                    } else if(paramKey.equals("filename")) {
                        filename = paramValue;
                    }
                }
            } else if (key.equals(RequestHeader.CONTENT_TYPE)) {
                contentType = MimeType.from(value.trim().toLowerCase());
            }
            i++;
        }

        StringBuilder sb = new StringBuilder();

        while(i < formLines.length) {
           sb.append(formLines[i]);
           i++;
        }

        byte[] bytes = sb.toString().getBytes(StandardCharsets.ISO_8859_1);

        Part part = new Part(dispositionType, name, filename, contentType, bytes);
        parts.put(name, part);
    }

    /**
     * request line 파싱하는 메서드
     * @param requestLine
     * @return
     */
    private String[] requestLineParse(String requestLine) {
        return requestLine.split("\\s");
    }

    /**
     * request body를 파싱하기 위한 메서드 ISO_8859_1로 decode 후 String 타입으로 변경
     * @param _contentLength
     * @param br
     * @return
     * @throws IOException
     */
    private String requestBodyParse(String _contentLength, ByteLineReader br) throws IOException {
        if(_contentLength == null) {
            return null;
        }
        int contentLength = Integer.parseInt(_contentLength);
        byte[] body = new byte[contentLength];
        br.read(body, contentLength);
        return new String(body, StandardCharsets.ISO_8859_1);
    }

    /**
     * request header를 파싱하는 메서드
     * @param br
     * @return
     * @throws IOException
     */
    private Map<String, String> requestHeaderParse(ByteLineReader br) throws IOException {
        String tmp;
        Map<String, String> headers = new HashMap<>();
        while((tmp = br.readLine()) != null && !tmp.isEmpty()) {
            int delimeterLoc = tmp.indexOf(":");
            if(delimeterLoc < 0) {
               throw new IllegalStateException("유효하지 않은 Http Header 형식");
            }
            String key = tmp.substring(0, delimeterLoc).trim().toLowerCase();
            String value;
            if(key.equals(RequestHeader.COOKIE) || key.equals(RequestHeader.CONTENT_TYPE)) {
                value = tmp.substring(delimeterLoc + 1).trim();
            } else {
                value = tmp.substring(delimeterLoc + 1).trim().toLowerCase();
            }
            headers.put(key, value);
        }
        return headers;
    }

    /**
     * queryString을 파싱하는 메서드
     * @param queryString 쿼리 스트링 전체
     * @param pairDelimeter key value 쌍 전체를 구분하기 위한 구분자
     * @param keyValueDelimeter (key, value) 를 파싱하기 위한 구분자
     * @return
     */
    private Map<String, String> queryParse(String queryString, String pairDelimeter, String keyValueDelimeter) {
        Map<String, String> tmpStore = new HashMap<>();
        if(queryString.isEmpty()) return tmpStore;
        String[] rawKeyValues= queryString.split(pairDelimeter);
        for(String rawKeyValue: rawKeyValues) {
            String[] keyValue = rawKeyValue.split(keyValueDelimeter);
            if(keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0].trim(), StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1].trim(), StandardCharsets.UTF_8);
                tmpStore.put(key, value);
            } else if(!rawKeyValue.contains(keyValueDelimeter)) {
                throw new IllegalArgumentException("잘못된 인자가 들어왔습니다.");
            }
        }
        return tmpStore;
    }

    /**
     * request의 경로를 파싱하기 위한 메서드
     * @param path
     * @return
     */
    private Map<String, String> pathParse(String path) {
        // api path 파싱
        String[] pathSplit = path.split("\\?");
        String apiPath = pathSplit[0];
        Map<String, String> queryParameters = new HashMap<>();
        queryParameters.put("path", apiPath);

        if(pathSplit.length == 1) return queryParameters;

        String rawQueryParameter = pathSplit[1];
        queryParameters = queryParse(rawQueryParameter, "&", "=");
        return queryParameters;
    }
}
