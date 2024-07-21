package utils;

import auth.Session;
import model.User;
import webserver.Request;

public class AuthUtil {

  /**
   * 로그인이 되어있는지 확인하는 로직
   * @param request
   * @return
   */
  public static User isLogin(Request request) {
    String sessionId = request.getCookie(Session.SESSION_ID);
    if(sessionId == null) return null;
    Session session = Session.getInstance();
    return session.get(sessionId);
  }
}
