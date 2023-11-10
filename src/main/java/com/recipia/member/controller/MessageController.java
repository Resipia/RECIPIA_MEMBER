package com.recipia.member.controller;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.recipia.member.aws.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/message")
public class MessageController {

    private final SnsService snsService;

//    @PostMapping("/publish")
//    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> messageMap) throws IOException {
//
//        Segment parentSeg = AWSXRay.beginSegment("parentSeg");
//        PublishResponse response = null;
//        try {
//            // 1. SnsService를 사용해서 메시지 발행
//            response =  snsService.publishNicknameToTopic(messageMap);
//
//        }catch (Exception e) {
//            parentSeg.end();
//        } finally {
//            parentSeg.close();
//        }
//
//
//        // 2. 발행 결과를 HTTP 응답으로 반환
//        return ResponseEntity.ok().body(response.messageId());
//
//    }

    @PostMapping("/publish")
    public ResponseEntity<String> publishMessage(@RequestBody Map<String, Object> messageMap) {
        try {
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
