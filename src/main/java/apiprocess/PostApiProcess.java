package apiprocess;


import db.H2Database;
import enums.HttpCode;
import enums.HttpMethod;
import exception.SQLRuntimeException;
import model.Part;
import model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileLoader;
import utils.Validation;
import webserver.Request;
import webserver.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class PostApiProcess implements ApiProcess {

  private static final Logger logger = LoggerFactory.getLogger(PostApiProcess.class);
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    Part title = request.getPart("title");
    Part content = request.getPart("content");
    Part image = request.getPart("image");

    if(Validation.anyNull(title, content)) {
      response.setHttpCode(HttpCode.BAD_REQUEST);
      model.put("articleFail", "제목과 글 내용 중 하나 이상이 비었습니다.");
      return "article/index";
    }

    String titleData = new String(title.getRawBody());
    String contentData = new String(content.getRawBody());

    String uuid = UUID.randomUUID().toString();

    byte[] imageData = image.getRawBody();
    String fileName = image.getFileName();
    String filePath = null;
    String uploadFileName = null;

    if(!fileName.isEmpty()) {
      // 저장할 파일 경로
      uploadFileName = uuid + "_" + fileName;
      filePath = FileLoader.FILE_DEFAULT_PATH + "/" + uploadFileName;

      try (FileOutputStream fos = new FileOutputStream(filePath)) {
        fos.write(imageData);
        logger.debug("이미지 파일 저장 완료: {}", uploadFileName);
      } catch (IOException e) {
        logger.debug("이미지 파일 저장 실패: {}", e.getMessage());
        response.setHttpCode(HttpCode.BAD_REQUEST);
        model.put("articleFail", "게시글 작성에 실패하셨어요.");
        return "article/index";
      }
    }


    H2Database repository = H2Database.getInstance();
    Post post = new Post(titleData, contentData, uploadFileName);
    repository.addPost(post);
    response.setHttpCode(HttpCode.Found);
    response.setLocation("/");
    return null;
  }
}
