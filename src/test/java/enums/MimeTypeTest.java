package enums;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MimeTypeTest {

  @Test
  @DisplayName("mimeType image/jpeg 테스트")
  void test() {
    String k = "image/jpeg";
    MimeType mimeType = MimeType.from(k);

    Assertions.assertThat(mimeType).isEqualTo(MimeType.JPG);
  }

}