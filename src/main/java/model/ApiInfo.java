package model;

import apiprocess.ApiProcess;
import enums.HttpMethod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * API 정보를 저장하는 객체
 * @Author minjun kim
 */
public class ApiInfo {
  private final String apiPath;
  private final HttpMethod httpMethod;
  private final ApiProcess apiProcess;

  public ApiInfo(String apiPath, HttpMethod httpMethod, ApiProcess apiProcess) {
    this.apiPath = apiPath;
    this.httpMethod = httpMethod;
    this.apiProcess = apiProcess;
  }

  public boolean isApiPathNotSame(String apiPath, String pattern) {
    Pattern compile = Pattern.compile(pattern);
    Matcher matcher = compile.matcher(apiPath);
    return !matcher.matches();
  }

  public boolean isMethodSame(HttpMethod httpMethod) {
    return this.httpMethod.equals(httpMethod);
  }

  public ApiProcess getApiProcess() {
    return apiProcess;
  }

  public String getApiPath() {
    return apiPath;
  }
}
