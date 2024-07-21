package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileLoader {
  private static Logger logger = LoggerFactory.getLogger(FileLoader.class);
  public static final String FILE_DEFAULT_PATH = "/Users/mj/images";

  public static byte[] load(String path) {
    byte[] fileBytes;
    String filePath = FILE_DEFAULT_PATH + path;
    logger.debug("filePath = {}", filePath);
    try {
      fileBytes = Files.readAllBytes(Paths.get(filePath));
    } catch (IOException e) {
      throw new IllegalStateException("파일 읽기 실패");
    };
    return fileBytes;
  }
}
