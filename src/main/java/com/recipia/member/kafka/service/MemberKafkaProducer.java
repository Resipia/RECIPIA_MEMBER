package com.recipia.member.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendResponse(String message) {
        kafkaTemplate.send("receive-response", message);
    }

}
