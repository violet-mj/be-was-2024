package utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 유효성 검사를 위한 유틸 클래스
 */
public class Validation {
  /**
   * String으로 들어온 인지 중 null이 있는지 확인
   * @param checkStr
   * @return
   */
  public static boolean anyNull(String... checkStr) {
    return Stream.of(checkStr).anyMatch(Validation::isEmpty);
  }

  /**
   * Object로 들어온 인자 중 null이 있는지 확인
   * @param checkStr
   * @return
   */
  public static boolean anyNull(Object... checkStr) {
    return Stream.of(checkStr).anyMatch(Objects::isNull);
  }

  /**
   * String이 null이거나 빈문자임을 확인
   * @param str
   * @return
   */
  private static boolean isEmpty(String str) {
    return Objects.isNull(str) || str.isEmpty();
  }

  /**
   * 이메일 형식을 확인하는 메서드
   * @param email
   * @return
   */
  public static boolean isEmail(String email) {
    Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }
}
