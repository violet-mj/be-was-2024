package webserver;

import enums.HttpMethod;
import model.Part;

import java.util.Map;

public class Request{

    private HttpMethod method;
    private String path;
    private String httpVersion;
    private Map<String, String> httpHeaders;
    private Map<String, String> parameters;
    private Map<String, String> cookies;
    private Map<String, Part> parts;
    private String body;

    public Request(RequestBuilder builder) {
      this.method = builder.method;
      this.path = builder.path;
      this.httpVersion = builder.httpVersion;
      this.httpHeaders = builder.httpHeaders;
      this.parameters = builder.parameters;
      this.cookies = builder.cookies;
      this.body = builder.body;
      this.parts = builder.parts;
    }

    public static class RequestBuilder {
      private HttpMethod method;
      private String path;
      private String httpVersion;
      private Map<String, String> httpHeaders;
      private Map<String, String> parameters;
      private Map<String, String> cookies;
      private Map<String, Part> parts;
      private String body;

      public RequestBuilder setMethod(HttpMethod method) {
        this.method = method;
        return this;
      }

      public RequestBuilder setPath(String path) {
        this.path = path;
        return this;
      }

      public RequestBuilder setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
        return this;
      }

      public RequestBuilder setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
        return this;
      }

      public RequestBuilder setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
      }

      public RequestBuilder setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
      }

      public RequestBuilder setBody(String body) {
        this.body = body;
        return this;
      }

      public RequestBuilder setParts(Map<String, Part> parts) {
        this.parts = parts;
        return this;
      }

      public Request build() {
        return new Request(this);
      }
    }

    public Request(HttpMethod method,
                   String path,
                   String httpVersion,
                   Map<String, String> httpHeaders,
                   Map<String, String> parameters,
                   String body,
                   Map<String, String> cookies
                   ) {
       this.method = method;
       this.path = path;
       this.httpVersion = httpVersion;
       this.httpHeaders = httpHeaders;
       this.parameters = parameters;
       this.body = body;
       this.cookies = cookies;
    }

    public String getHeader(String HeaderKey) {
        return httpHeaders.get(HeaderKey);
    }

    public String getParameter(String parameterKey) {
        return parameters.get(parameterKey);
    }

    public String getCookie(String cookieKey) {
      return cookies.get(cookieKey);
    }
    public Part getPart(String name) {
      return parts.get(name);
    }

    public String getBody() {
      return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
