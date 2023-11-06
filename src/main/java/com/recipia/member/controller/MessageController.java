package com.recipia.member.controller;

import com.recipia.member.aws.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final SnsService snsService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> messageMap) {
        // 1. SnsService를 사용해서 메시지 발행
        PublishResponse response =  snsService.publishNicknameToTopic(messageMap);

        // 2. 발행 결과를 HTTP 응답으로 반환
        return ResponseEntity.ok().body(response.messageId());

    }

}
