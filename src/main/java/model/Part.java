package model;

import enums.MimeType;

public class Part {
  public static final String CONTENT_DISPOSITION = "content-disposition";
  private String dispositionType;
  private String name;
  private String fileName;
  private MimeType contentType;
  private byte[] rawBody;

  public Part(String dispositionType, String name, String fileName, MimeType contentType, byte[] rawBody) {
    this.dispositionType = dispositionType;
    this.name = name;
    this.fileName = fileName;
    this.rawBody = rawBody;
    this.contentType = contentType;
  }

  public Part(String dispositionType, String name, MimeType contentType, byte[] rawBody) {
    this(dispositionType, name, null, contentType, rawBody);
  }

  public String getDispositionType() {
    return dispositionType;
  }

  public String getName() {
    return name;
  }

  public String getFileName() {
    return fileName;
  }

  public MimeType getContentType() {
    return contentType;
  }

  public byte[] getRawBody() {
    return rawBody;
  }
}
