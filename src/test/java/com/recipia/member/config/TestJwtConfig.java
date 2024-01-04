package com.recipia.member.config;

import com.recipia.member.adapter.out.persistence.constant.RoleType;
import com.recipia.member.config.dto.TokenMemberInfoDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

/**
 * 테스트용 jwt를 세팅하는 유틸 클래스
 */
public class TestJwtConfig {

    public static void setupMockAuthentication(Long memberId, String email, String nickname) {
        TokenMemberInfoDto tokenMemberInfoDto = TokenMemberInfoDto.of(memberId, email, nickname);
        // 권한 목록을 생성한다.
        GrantedAuthority authority = new SimpleGrantedAuthority(RoleType.MEMBER.name());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                tokenMemberInfoDto, null, Collections.singletonList(authority));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

}