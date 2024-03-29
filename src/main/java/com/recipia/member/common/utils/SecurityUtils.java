package com.recipia.member.common.utils;

import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.dto.TokenMemberInfoDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 스프링 시큐리티에서 설정한 security context에서 원하는 데이터 추출 Utils 클래스
 */
@Component
public class SecurityUtils {


    // 현재 인증된 사용자의 TokenMemberInfoDto를 가져온다.
    private TokenMemberInfoDto getCurrentTokenMemberInfoDto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new MemberApplicationException(ErrorCode.MEMBER_INFO_NOT_FOUND_IN_SECURITY);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof TokenMemberInfoDto) {
            TokenMemberInfoDto tokenMemberInfoDto = (TokenMemberInfoDto) principal;
            validateTokenMemberInfoDto(tokenMemberInfoDto);
            return tokenMemberInfoDto;
        }

        throw new MemberApplicationException(ErrorCode.MEMBER_INFO_NOT_FOUND_IN_SECURITY);
    }

    // memberId와 email이 존재하는지 검증한다.
    private void validateTokenMemberInfoDto(TokenMemberInfoDto tokenMemberInfoDto) {
        if (tokenMemberInfoDto.id() == null || tokenMemberInfoDto.email() == null) {
            throw new MemberApplicationException(ErrorCode.MEMBER_INFO_NOT_FOUND_IN_SECURITY);
        }
    }

    // 현재 인증된 사용자의 memberId를 가져온다.
    public Long getCurrentMemberId() {
        TokenMemberInfoDto tokenMemberInfoDto = getCurrentTokenMemberInfoDto();
        return tokenMemberInfoDto.id();
    }

    // 현재 인증된 사용자의 email을 가져온다.
    public String getCurrentMemberEmail() {
        TokenMemberInfoDto tokenMemberInfoDto = getCurrentTokenMemberInfoDto();
        return tokenMemberInfoDto.email();
    }
}
