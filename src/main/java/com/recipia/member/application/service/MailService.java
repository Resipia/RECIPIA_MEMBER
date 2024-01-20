package com.recipia.member.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * mail 전송을 위한 서비스
 */
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender emailSender;

    public void sendTemporaryPassword(String to, String temporaryPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[RECIPIA] 비밀번호 재발급");
        message.setText("아래는 임시 비밀번호입니다.\n\n" + temporaryPassword + "\n\n* 반드시 로그인 후 비밀번호를 변경해주세요.");
        emailSender.send(message);
    }

}
