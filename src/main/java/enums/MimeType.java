package enums;

/**
 * 미디어 타입을 지정해놓은 클래스
 */
public enum MimeType {
    PLAIN("text/plain"),
    HTML("text/html"),
    CSS("text/css"),
    JAVASCRIPT("text/javascript"),
    ICO("image/vnd.microsoft.icon"),
    PNG("image/png"),
    JPG("image/jpeg"),
    SVG("image/svg+xml"),
    FORM("application/x-www-form-urlencoded"),
    MULTIPART_FORM("multipart/form-data"),
    OCTET("application/octet-stream");

    private final String mimeType;

    MimeType(String mimeType) {
       this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String toString() {
        return getMimeType();
    }

    public static MimeType from(String mimeType) {
        for (MimeType type : values()) {
            if (type.getMimeType().equals(mimeType)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant with mimeType " + mimeType);
    }

}
