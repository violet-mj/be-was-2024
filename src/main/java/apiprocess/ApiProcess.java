package apiprocess;

import webserver.Request;
import webserver.Response;

import java.util.Map;

/**
 * api 경로에 따른 로직 처리를 추상화하기 위한 인터페이스
 * @Author minjun kim
 */
public interface ApiProcess {
    /**
     * http request, http response, model을 인자로 api 로직을 처리함
     * @param request
     * @param response
     * @param model
     * @return
     */
    String process(Request request, Response response, Map<String, Object> model);
}
