package apiprocess;

import webserver.Request;
import webserver.Response;

import java.util.Map;

/**
 * api path : "/user/login"
 * 로그인 처리를 위한 클래스 (H2, jdbc 사용)
 * @Author minjun kim
 */
public class LoginPageApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    return "login/index";
  }
}
