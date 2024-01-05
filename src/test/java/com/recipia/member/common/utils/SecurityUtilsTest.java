package com.recipia.member.common.utils;

import com.recipia.member.config.TestJwtConfig;
import com.recipia.member.config.TotalTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("[통합] 시큐리티 유틸 클래스")
class SecurityUtilsTest extends TotalTestSupport {

    @Autowired
    private SecurityUtils securityUtils;

    @BeforeEach
    void setup() {
        TestJwtConfig.setupMockAuthentication(12345L, "user@example.com", "nickname");
    }

    @Test
    @DisplayName("[happy] 현재 사용자의 memberId를 정상적으로 가져온다.")
    void getCurrentMemberIdTest() {
        Long memberId = securityUtils.getCurrentMemberId();
        assertEquals(12345L, memberId);
    }

    @Test
    @DisplayName("[happy] 현재 사용자의 email을 정상적으로 가져온다.")
    void getCurrentMemberEmailTest() {
        String email = securityUtils.getCurrentMemberEmail();
        assertEquals("user@example.com", email);
    }
}