package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 파일을 로드하기 위한 클래스
 */
public class ResourcesLoader {

  private static final Logger logger = LoggerFactory.getLogger(ResourcesLoader.class);

  /**
   * build.gradle에서 설정한 클래스 패스에서 파일을 로드하는 역할을 한다.
   * @param filePath 파일 경로
   * @return
   */
  public static byte[] getFile(String filePath) {
    byte[] body;
    if(filePath.startsWith("/")) {
      filePath = filePath.substring(1);
    }
    try (
            InputStream inputStream = ResourcesLoader.class.getClassLoader().getResourceAsStream(filePath)
    ) {
      body = inputStream.readAllBytes();
    } catch (Exception e) {
      logger.debug("Exception = {}", e.getMessage());
      throw new IllegalArgumentException("존재하지 않는 파일입니다.");
    }
    return body;
  }
}
