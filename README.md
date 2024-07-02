# be-was-2024 week1-1 김민준
### Java Thread

Java Thread

- `Thread` 로 쓰레드를 생성하는 경우 쓰레드를 직접만든다.
    - 즉 유저가 쓰레드를 관리함

Executors

- Thread만들고 관리하는 것을 고수준의 API Executors에 위임
    - Runnable만 만들고 생성, 종료, 없애기는 Executors에게 위임
- Thread Pool을 만들어 관리
    - Thread 생명 주기를 관리합니다.

개선점

- 위 쓰레드는 OS Thread와 1대 1로 대응하여 실행된다.
    - OS가 Thread를 스케쥴링 한다.
    - JVM이 Thread를 스케쥴링 하는 방법은 없을까?

### HTTP Request 출력

```java
String tmp;
BufferedReader br = new BufferedReader(new InputStreamReader(in));
while((tmp = br.readLine()) && !tmp.empty()) {
	logger.debug(tmp);
}
```

### NIO 라이브러리 사용하지 않고 반환하기

BufferedInputStream

- 다른 `InputStream` 으로 부터 읽는다.
    - `FileInputStream` 은 파일에서 데이터를 읽는다.
- Buffer를 만들어 한번에 보내는 역할
    - `FileInputStream` 은 1byte씩 보낸다.
        - 이는 `FileInputStream.read()` 를 호출할 때 마다 syscall(매우 비쌈)을 호출하는 것이다.

```java
DataOutputStream dos = new DataOutputStream(out);
ByteArrayOutputStream bos = new ByteArrayOutputStream();
FileInputStream fis = new FileInputStream("src/main/resources/static" + request.getPath());
BufferedInputStream bis = new BufferedInputStream(fis);
bis.transferTo(bos);
byte[] body = bos.toByteArray();
```
