package com.recipia.member.controller;

import com.recipia.member.aws.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final SnsService snsService;

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> messageMap) {
        snsService.publishNicknameToTopic(messageMap);
        return ResponseEntity.ok("Message published to SNS topic.");
    }

}
