

import java.util.Arrays;
import java.util.List;

// 기존에 만든 HttpServer의 인스턴스를 2개 만들어서 테스트해보자는 목적의 클래스
public class Application {
    // 8081은 수신 대기 포트이고, /task는 작업이 도달하는 엔드포인트!
    private static final String WORKER_ADDRESS_1 = "http://localhost:8081/task";
    private static final String WORKER_ADDRESS_2 = "http://localhost:8082/task";

    public static void main(String[] args) {
        Aggregator aggregator = new Aggregator();
        String task1 = "10,200";
        String task2 = "123456789,100000000000000,700000002342343";

        // 병렬적으로 진행되도록 sendTask()에 워커 목록과 작업 목록을 넣고 호출!
        List<String> results = aggregator.sendTasksToWorkers(Arrays.asList(WORKER_ADDRESS_1, WORKER_ADDRESS_2),
                Arrays.asList(task1, task2));

        // 결과를 받은 후, 이를 하나씩 출력해보자!
        for (String result : results) {
            System.out.println(result);
        }
    }
}
