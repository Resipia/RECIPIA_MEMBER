package com.recipia.member.common.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 임시 비밀번호 생성 util 클래스
 */
@Component
public class TempPasswordUtil {

    private final char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
            'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    public String generateTempPassword() {
        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            int idx = random.nextInt(charSet.length);
            str.append(charSet[idx]);
        }

        return str.toString();
    }
}
