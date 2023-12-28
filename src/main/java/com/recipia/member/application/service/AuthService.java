package com.recipia.member.application.service;

import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.domain.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthUseCase {

    private final TokyoSnsService tokyoSnsService;

    @Override
    public void verityPhoneNumber(Authentication authentication) {
        authentication.formatPhoneNumber();
        String verificationCode = generateRandomCode();
        tokyoSnsService.sendVerificationCode(authentication.getPhoneNumber(), verificationCode);

    }

    // 랜덤 6자리 숫자 생성
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000부터 999999까지
        return String.valueOf(number);
    }

}
