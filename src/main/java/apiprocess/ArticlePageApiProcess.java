package apiprocess;

import db.H2Database;
import enums.HttpCode;
import model.Post;
import webserver.Request;
import webserver.Response;

import java.util.Map;

public class ArticlePageApiProcess implements ApiProcess {
  @Override
  public String process(Request request, Response response, Map<String, Object> model) {
    String[] paths = request.getPath().split("\\/");
    String path = paths[paths.length - 1];

    H2Database repository = H2Database.getInstance();
    Post post = repository.findPostById(path);

    if(post == null) {
      response.setHttpCode(HttpCode.NOT_FOUND);
      return "error/404";
    }

    model.put("title", post.getTitle());
    model.put("content", post.getContent());
    model.put("image", post.getImage());
    model.put("nextPostId", Integer.parseInt(path) + 1);
    return "article/post";
  }
}
