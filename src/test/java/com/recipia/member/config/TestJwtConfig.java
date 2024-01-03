package com.recipia.member.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 테스트용 jwt를 세팅하는 유틸 클래스
 */
public class TestJwtConfig {

    public static void setupMockJwt(Long memberId, String nickname) {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("memberId")).thenReturn(memberId);
        when(jwt.getClaim("nickname")).thenReturn(nickname);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

}