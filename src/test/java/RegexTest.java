import model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.AuthUtil;
import utils.Template;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class RegexTest {

  private static Logger logger = LoggerFactory.getLogger(RegexTest.class);
  Pattern p = Pattern.compile("<<(.*?)>>");
  Pattern template = Pattern.compile("<my-template\\s*(mt::(.+?)=\"(.+?)\")?\\s*>(.*?)</my-template\\s*>");

  @Test
  void regexTest() {
    // given
    Matcher m1 = p.matcher("minjun");
    Matcher m2 = p.matcher("<<minjun>>");

    // when
    assertThat(m1.matches()).isFalse();

    assertThat(m2.matches()).isTrue();
    assertThat(m2.group(1)).isEqualTo("minjun");
  }

  @Test
  @DisplayName("매칭되는 패턴이 여러개 있을 때")
  void multiMatchingRegexTest() {
    // given
    Matcher matcher = p.matcher("<<minjun>> hyperhyper <<sonet>> dfasdfa;lskdfj <dscodsakfas>>");

    // when
    while(matcher.find()) {
      String group = matcher.group();
      logger.debug(group);
    }
  }

  @Test
  @DisplayName("동적 html: 나만의 태그 인식 attribute가 존재하지 않을 때")
  void dynamicHtmlTest() {
    // given
    String tag = "<my-template>minjun</my-template>";

    // when
    Matcher matcher1 = template.matcher(tag);

    // then
    assertThat(matcher1.matches()).isTrue();
    assertThat(matcher1.group(1)).isEqualTo(null);
    assertThat(matcher1.group(2)).isEqualTo(null);
    assertThat(matcher1.group(3)).isEqualTo(null);
    assertThat(matcher1.group(4)).isEqualTo("minjun");
  }

  @Test
  @DisplayName("동적 html: 나만의 태그 인식 attribute가 존재할 때")
  void templateWithAttributeTest() {
    // given
    String tagWithAttribute = "<my-template mt::if=\"login\">minjun</my-template>";

    // when
    Matcher matcher2 = template.matcher(tagWithAttribute);

    // then
    assertThat(matcher2.matches()).isTrue();
    assertThat(matcher2.group(1)).isEqualTo("mt::if=\"login\"");
    assertThat(matcher2.group(2)).isEqualTo("if");
    assertThat(matcher2.group(3)).isEqualTo("login");
    assertThat(matcher2.group(4)).isEqualTo("minjun");
  }

  @Test
  @DisplayName("동적 html: binding할 패턴 인식")
  void findBindingPatternTest() {
    // given
    Pattern compile = Pattern.compile("\\{([\\w\\.]+)\\}");

    // when
    Matcher matcher = compile.matcher("{user.name}");

    // then
    assertThat(matcher.matches()).isTrue();
    assertThat(matcher.group(1)).isEqualTo("user.name");
  }

  @Test
  @DisplayName("리플렉션을 통해 값 바인딩")
  void bindingValueWithReflectionTest() throws IllegalAccessException {
    // given
    String body = "{user.name}입니다. 잘 부탁 드려요. \n" +
            "이메일은 {user.email}로 연락부탁드립니다.\n";
    String[] value = {"user", "name"};
    User user = new User("minjun123", "1234", "minjun", "minjun@naver.com");
    Map<String, Object> model = new HashMap<>() {{
      put("user", user);
    }};

    // when
    String s = Template.doBinding(body, model);

    // then
    String rs="minjun입니다. 잘 부탁 드려요. \n" +
            "이메일은 minjun@naver.com로 연락부탁드립니다.\n";
    assertThat(s).isEqualTo(rs);
  }

  @Test
  @DisplayName("binding할 객체가 존재하지 않거나 String 타입이 아닌 경우")
  void bindingNotExistValue() throws IllegalAccessException, NoSuchMethodException {
    // given
    String body = "{user.name}입니다. 잘 부탁 드려요. \n" +
            "이메일은 {user.emaildfdfa}로 연락부탁드립니다.\n";
    String[] value = {"user", "name"};
    User user = new User("minjun123", "1234", "minjun", "minjun@naver.com");
    Map<String, Object> model = new HashMap<>() {{
      put("user", user);
    }};

    // when

    String s = Template.doBinding(body, model);

    // then
    String rs="minjun입니다. 잘 부탁 드려요. \n" +
            "이메일은 null로 연락부탁드립니다.\n";
    assertThat(s).isEqualTo(rs);
  }

  @Test
  @DisplayName("template 렌더링 테스트")
  void templateRenderingTest() throws IllegalAccessException {
    // given
    String body = "<my-template mt::if=\"user\">\n" +
            "{user.name}입니다. 잘 부탁 드려요. \n" +
            "이메일은 {user.email}로 연락부탁드립니다.\n" +
            "</my-template>";

    User user = new User("minjun123", "1234", "minjun", "minjun@naver.com");
    Map<String, Object> model = new HashMap<>() {{
      put("user", user);
    }};

    // when
    byte[] s = Template.render(body.getBytes(), model);

    String ss = new String(s, StandardCharsets.UTF_8);
    logger.debug("value={}", ss);

    // then
    String rs="minjun입니다. 잘 부탁 드려요. \n" +
            "이메일은 minjun@naver.com로 연락부탁드립니다.";
    assertThat(s).isEqualTo(rs.getBytes());
  }

  @Test
  @DisplayName("패턴 매칭 테스트")
  void patternTest() {
    // given
    String body = "<my-template mt::if=\"user\">\n" +
            "{user.name}입니다. 잘 부탁 드려요. \n" +
            "이메일은 {user.email}로 연락부탁드립니다.\n" +
            "</my-template>";
    Pattern template = Pattern.compile("<my-template\\s*(mt::(.+?)=\"(.+?)\")?\\s*>(.*?)</my-template\\s*>", Pattern.DOTALL);

    // when
    Matcher bodyMatcher = template.matcher(body);

    // when
    assertThat(bodyMatcher.matches()).isTrue();
  }



  @Test
  @DisplayName("Matcher의 appendReplacement 테스트")
  void appendReplacementTest() {
    Pattern p = Pattern.compile("cat");
    Matcher m = p.matcher("one cat two cats in the yard");
    StringBuilder sb = new StringBuilder();
    while (m.find()) {
      m.appendReplacement(sb, "dog");
    }
    m.appendTail(sb);
    logger.debug(sb.toString());
  }

  @Test
  void t() {
    String k = "------WebKitFormBoundarymlsSN8ar4QkP9bFQ\r\n" +
            "Content-Disposition: form-data; name=\"title\"\r\n" +
            "\r\n" +
            "sadf\r\n" +
            "------WebKitFormBoundarymlsSN8ar4QkP9bFQ\r\n" +
            "Content-Disposition: form-data; name=\"content\"\r\n" +
            "\r\n" +
            "asdf\r\n" +
            "------WebKitFormBoundarymlsSN8ar4QkP9bFQ--\r\n";
    logger.debug("size = {}", k.getBytes(StandardCharsets.ISO_8859_1).length);
  }
}
