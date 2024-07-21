package exception;

/**
 * SQLException을 언체크드 예외로 바꾸기 위한 예외 클래스
 * @Author minjun kim
 */
public class SQLRuntimeException extends RuntimeException {
  public SQLRuntimeException(String message) {
    super(message);
  }
  public SQLRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
