package utils;

import enums.MimeType;

import java.util.HashMap;
import java.util.Map;

public class MimeTypeMapper {
    private Map<String, MimeType> mimeTypeMapper;

    public MimeTypeMapper() {
        mimeTypeMapper = new HashMap<>() {{
            put(".html", MimeType.HTML);
            put(".css", MimeType.CSS);
            put(".js", MimeType.JAVASCRIPT);
            put(".svg", MimeType.SVG);
            put(".ico", MimeType.ICO);
            put(".png", MimeType.PNG);
            put(".jpg", MimeType.JPG);
            put(".jpeg", MimeType.JPG);
        }};
    }

    public MimeType getMimeType(String ext) {
        if(mimeTypeMapper.containsKey(ext)) {
            return mimeTypeMapper.get(ext);
        }
        throw new IllegalArgumentException("확장자와 매핑되는 mimeType이 없습니다.");
    }
}
