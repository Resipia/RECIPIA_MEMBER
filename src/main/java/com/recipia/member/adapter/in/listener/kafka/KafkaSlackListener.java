package com.recipia.member.adapter.in.listener.kafka;

import com.recipia.member.application.service.SlackNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaSlackListener {

    private final SlackNotificationService slackNotificationService;

    @KafkaListener(topics = "${spring.kafka.template.default-topic}", groupId ="${spring.kafka.consumer.group-id}")
    public void listen(String message) {
        // 카프카 메시지를 받았을 때 처리
        log.info("Received message in group error-handler-group: " + message);
        // 메시지를 Slack으로 전송
        slackNotificationService.sendMessageToSlack (message) ;
    }
}


