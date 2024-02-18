package com.recipia.member.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Slack 어플에 알림을 전송하는 서비스 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SlackNotificationService {

    @Value("${slack.web-hook-url}")
    private String slackWebHookUrl;

    private final RestTemplate restTemplate;

    /**
     * slack에 에러 메시지를 전송한다.
     */
    public void sendMessageToSlack(String message) {
        String payload = "{\"text\": \"" + message + "\"}";
        restTemplate.postForEntity(slackWebHookUrl, payload, String.class);
        log.info("slack에 에러 메시지 발행 완료") ;
    }

}
