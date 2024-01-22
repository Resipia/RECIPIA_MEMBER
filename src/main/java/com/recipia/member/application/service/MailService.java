package com.recipia.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * mail 전송을 위한 서비스
 */
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender emailSender;

    @Async
    public CompletableFuture<Boolean> sendTemporaryPassword(String to, String temporaryPassword) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("[RECIPIA] 비밀번호 재발급");
            message.setText("아래는 임시 비밀번호입니다.\n\n" + temporaryPassword + "\n\n* 반드시 로그인 후 비밀번호를 변경해주세요.");
            emailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

}
