package com.recipia.member.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberKafkaConsumer {

    private final MemberKafkaProducer memberKafkaProducer;

    @KafkaListener(topics = "send-username", groupId = "recipia")
    public void listen(String username) {
        // 필요한 로직 처리
        String response = processUsername(username);

        // 응답 전송
        memberKafkaProducer.sendResponse(response);
    }

    private String processUsername(String username) {
        // 여기에 로직 추가
        return "Processed: " + username;
    }

}
