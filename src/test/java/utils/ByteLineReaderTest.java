package utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class ByteLineReaderTest {

    String httpMessage = "POST /index.html http/1.1\r\n" +
          "Connection :    keep-alive\r\n" +
          "Host: localhost:8080\r\n" +
          "Content-Length: 46\r\n" +
          "Content-Type: application/x-www-form-urlencoded\r\n\r\n" +
          "username=&userId=minjun123&password=1234\r\n";
    byte[] rawHttpMessage = httpMessage.getBytes();
    InputStream inputStream = new ByteArrayInputStream(rawHttpMessage);

    @Test
    @DisplayName("readLine 테스트")
    void readLineTest() throws IOException {
      // given
      ByteLineReader byteLineReader = new ByteLineReader(inputStream);

      // when
      String bytes1 = byteLineReader.readLine();
      String bytes2 = byteLineReader.readLine();

      // then
      assertThat(bytes1).isEqualTo("POST /index.html http/1.1");
      assertThat(bytes2).isEqualTo("Connection :    keep-alive");
    }

  @Test
  @DisplayName("read테스트")
  void readTest() throws IOException {
    // given
    ByteLineReader byteLineReader = new ByteLineReader(inputStream);

    // when
    byte[] buf = new byte[6];
    byteLineReader.read(buf, 6);
    String originalValue = new String(buf);

    // then
    assertThat(originalValue).isEqualTo("POST /");
  }
}