package com.recipia.member.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Kafka 토픽을 나열하는 클래스이다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TopicLister {

    // Kafka AdminClient, Spring의 DI를 통해 주입받는다.
    private final AdminClient adminClient;

    /**
     * 현재 Kafka에 존재하는 모든 토픽을 나열하는 메서드이다.
     */
    public void listTopics() {
        // AdminClient를 사용해 모든 토픽 정보를 가져온다.
        ListTopicsResult listTopicsResult = adminClient.listTopics();

        try {
            // 토픽 이름만 추출하기 위해 Future 객체를 동기적으로 해결한다.(get()으로 바로 받는다.)
            Set<String> topicNames = listTopicsResult.names().get();

            // 각 토픽 이름을 로그로 출력한다.
            for (String topicName : topicNames) {
                log.info("발견된 토픽: {}", topicName);
            }
        } catch (InterruptedException | ExecutionException e) {
            // 예외 상황에 대한 로깅을 한다.
            log.error("토픽 목록을 가져오는 도중 오류가 발생했습니다.", e);
        }
    }

}
