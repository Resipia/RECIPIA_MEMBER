package com.recipia.member.common.utils;

import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("[통합] 임시 비밀번호 util 클래스 테스트")
class TempPasswordUtilTest extends TotalTestSupport {

    @Autowired
    private TempPasswordUtil sut;

    @DisplayName("[happy] 생성된 임시 비밀번호의 길이는 10이다.")
    @Test
    void generateTempPasswordTest() {
        // when
        String tempPassword = sut.generateTempPassword();

        // then
        assertNotNull(tempPassword);        // 임시 비밀번호가 null이 아니어야 한다.
        assertEquals(10, tempPassword.length());    // 임시 비밀번호의 길이는 10이어야 한다.
    }

}