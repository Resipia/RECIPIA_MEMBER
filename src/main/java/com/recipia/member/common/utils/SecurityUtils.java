package com.recipia.member.common.utils;

import com.recipia.member.config.dto.TokenMemberInfoDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {


    /**
     * 현재 인증된 사용자의 TokenMemberInfoDto를 가져온다.
     */
    private static TokenMemberInfoDto getCurrentTokenMemberInfoDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof TokenMemberInfoDto) {
            return (TokenMemberInfoDto) principal; // 직접 TokenMemberInfoDto 객체를 반환
        }

        return null;
    }

    /**
     * 현재 인증된 사용자의 memberId를 가져온다.
     */
    public static Long getCurrentMemberId() {
        TokenMemberInfoDto tokenMemberInfoDto = getCurrentTokenMemberInfoDto();
        return tokenMemberInfoDto != null ? tokenMemberInfoDto.id() : null;
    }

    /**
     * 현재 인증된 사용자의 email을 가져온다.
     */
    public static String getCurrentMemberEmail() {
        TokenMemberInfoDto tokenMemberInfoDto = getCurrentTokenMemberInfoDto();
        return tokenMemberInfoDto != null ? tokenMemberInfoDto.email() : null;
    }

}
