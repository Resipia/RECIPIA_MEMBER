package com.recipia.member.controller;

import com.recipia.member.aws.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final SnsService snsService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> messageMap) {
        try {
            log.info("[MEMBER] Published message to SNS with messageId: TEST!@!!@!@!");

            // SnsService를 사용해서 메시지 발행
            PublishResponse response = snsService.publishNicknameToTopic(messageMap);

            // 발행 결과를 HTTP 응답으로 반환
            return ResponseEntity.ok().body(response.messageId());
        } catch (Exception e) {
            // HTTP 상태 코드 500 (Internal Server Error) 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Message publishing failed: " + e.getMessage());
        }
    }


}
