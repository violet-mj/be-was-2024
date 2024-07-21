package model;

import utils.FileLoader;
import utils.PathUtils;
import utils.ResourcesLoader;
import utils.Template;

import java.util.Map;

/**
 * 모델과 뷰를 지정해놓은 클래스
 * 정적 데이터와 템플릿을 렌더링 할 수 있음
 * @Author minjun kim
 */
public class ModelView {
  private final String view;
  private final Map<String, Object> model;

  public ModelView(String view, Map<String, Object> model) {
    this.view = view;
    this.model = model;
  }

  public void addAttribute(String key, Object value) {
    model.put(key, value);
  }

  public byte[] render() throws IllegalAccessException {
    String viewName = PathUtils.filePathResolver(view);
    byte[] file;
    if(isImage(viewName)) {
      file = FileLoader.load(viewName);
    } else {
      file = ResourcesLoader.getFile(viewName);
    }
    if(viewName.endsWith(".html")) {
      return Template.render(file, model);
    } else {
      return file;
    }
  }

  private boolean isImage(String viewName) {
    return viewName.endsWith(".jpg") ||
            viewName.endsWith(".jpeg") ||
            viewName.endsWith(".png");
  }
}
