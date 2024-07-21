package apiprocess;
import db.H2Database;
import model.Post;
import webserver.Request;
import webserver.Response;

import java.util.Collection;
import java.util.Map;

/**
 * api path : "/"
 * 홈 화면을 출력하는 로직 처리 구현체
 * @Author minjun kim
 */
public class HomepageApiProcess implements ApiProcess {
    @Override
    public String process(Request request, Response response, Map<String, Object> model) {
        H2Database repository = H2Database.getInstance();
        Collection<Post> allPost = repository.findAllPost();
        model.put("allPost", allPost);
        model.put("postId", 1);
        return "index";
    }
}
