package utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteLineReader {
  private final InputStream inputStream;
  private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private boolean endOfStreamReached = false;

  public ByteLineReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public String readLine() throws IOException {
      if (endOfStreamReached) {
        return null;
      }
      buffer.reset();
      int read;
      while ((read = inputStream.read()) != -1) {
        if (read == '\n') {
          break;
        } else if (read != '\r') {
          buffer.write(read);
        }
      }

      if (read == -1) {
        endOfStreamReached = true;
        if (buffer.size() == 0) {
          return null;
        }
      }

      return new String(buffer.toByteArray());
  }

  public int read() throws IOException {
    return inputStream.read();
  }

  public void read(byte[] buf, int length) throws IOException {
    inputStream.read(buf, 0, length);
  }
}
