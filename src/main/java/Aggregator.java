import networking.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// WebClient의 여러 request를 보내고 취합하는 클래스
public class Aggregator {
    private WebClient webClient;

    // WebClient 객체 참조를 들고 있다가 생성자에서 인스턴스를 만들 목적!
    public Aggregator() {
        this.webClient = new WebClient();
    }

    // http 서버의 워커 노드 주소 목록과 작업 목록을 매개로 받는 메소드
    public List<String> sendTasksToWorkers(List<String> workersAddresses, List<String> tasks) {
        // 추후에 워커노드들로부터 온 response를 저장하는 변수!
        CompletableFuture<String>[] futures = new CompletableFuture[workersAddresses.size()];

        // 모든 워커 주소를 반복돌면서 워커 주소와 해당 워커에 보낼 작업도 가져옴ㅇㅇ
        // 그 후, task 문자열을 byte[]로 변환.
        // 마지막으로 이 task 내용을 워커에 보내기 위해 WebClient의 sendTask()를 호출!!
        for (int i = 0; i < workersAddresses.size(); i++) {
            String workerAddress = workersAddresses.get(i);
            String task = tasks.get(i);

            byte[] requestPayload = task.getBytes();
            futures[i] = webClient.sendTask(workerAddress, requestPayload);
        }

        // 모든 요청을 서버로 보냈으니 결과를 저장할 배열 변수를 하나 맹글자!
        // 목록에 대해 반복문을 돌면서 futures의 각 인덱스에서 join()을 호출하여 해당 요청의 response를 기다려 받는당.
        // 모든 워커로부터 응답을 받고 나면, 이를 몽땅 모아서 뭉탱이로 반환해주기!
        List<String> results = Stream.of(futures).map(CompletableFuture::join).collect(Collectors.toList());

        return results;
    }
}
