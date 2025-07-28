

package networking;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class WebClient {
    private HttpClient client; // HttpClient 객체를 담을 필드 생성

    public WebClient() {
        this.client = HttpClient.newBuilder() // Builder를 리턴하는 static 메소드
                .version(HttpClient.Version.HTTP_1_1) // enum을 매개로 받아 Builder 객체를 리턴하므로, 메소드 체이닝 가능!
                .build(); // 실제 객체를 생성하는 메소드
    }

    // sendTask()는 요청을 보낼 주소와 byte[]의 http 메세지 본문을 매개로 받는 메소드!
    public CompletableFuture<String> sendTask(String url, byte[] requestPayload) {
        // 가장 먼저할 일은 Http request를 생성하는 것!
        HttpRequest request = HttpRequest.newBuilder()
                // post를 통해 요청 메세지 본문에 넣고,
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
                // dest 주소를 설정함!
                .uri(URI.create(url))
                .build();

        // 만든 요청을 비동기적으로 전송만 하고, 응답을 기다리지는 않도록 sendAsync()를 호출!
        // 두번째 매개는 응답이 오는 경우 이를 처리할 객체의 인스턴스!
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // 응답은 String 타입이므로 ofString()을 호출하여 BodyHandler를 만들어 넘겨줌!
                // 응답이 도착시 body()를 호출하여 헤더없이 본문만 가져옴ㅇㅇㅇ
                .thenApply(HttpResponse::body); // 임의객체에 대한 인스턴스 메소드 레퍼런스
    }
}
