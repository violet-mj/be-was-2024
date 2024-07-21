package webserver;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;

import enums.HttpResult;
import model.ModelView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.*;

public class RequestHandler implements Callable<Void> {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public Void call() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            DataOutputStream dos = new DataOutputStream(out);

            // request, response 객체 생성
            RequestParser requestParser = RequestParser.getRequestParser();
            Request request = requestParser.getRequest(in);
            Response response = new Response();

            CommonApiProcess apiProcess = CommonApiProcess.getInstance();
            ModelView view = apiProcess.getView(request, response);

            // 리다이렉트 시
            if(isRedirect(response)) {
               response.sendRedirect(dos);
               return null;
            }

            // 클라이언트에게 파일 반환
            response.send(dos, view.render());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private static boolean isRedirect(Response response) {
        return response.getHttpCode() != null
                && response.getHttpCode().getHttpResult().equals(HttpResult.REDIRECT);
    }
}
