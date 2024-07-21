package enums;

/**
 * Http Method를 저장해 놓은 클래스
 * @Author minjun kim
 */
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private String methodName;

    HttpMethod(String methodName) {
        this.methodName = methodName;
    }

    public static HttpMethod from(String methodName) {
        HttpMethod[] methods = HttpMethod.values();
        for(HttpMethod method: methods) {
           if(method.methodName.equals(methodName)) {
               return method;
           }
        }
        throw new IllegalArgumentException("해당하는 이름의 http 메소드는 존재하지 않습니다.");
    }
}
