package com.recipia.member.config.jwt;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 유효한 access token인지 검증하는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TokenValidator {

    private final MemberPort memberPort;

    public boolean isValidToken(String token, String tokenType) {
        try {
            // 1. claims에서 type을 추출한다.
            Claims claims = TokenUtils.getClaimsFromToken(token);
            String type = claims.get("type", String.class);

            // 2. 토큰 타입을 확인한다. ex) access
            if (!type.equals(tokenType)) {
                return false;
            }

            String email = claims.get("email", String.class);
            Member member = memberPort.findMemberByEmailAndStatus(email, MemberStatus.ACTIVE).orElse(null);
            return member != null;
        } catch (JwtException jwtException) {
            log.debug("JWT validation error: {}", jwtException.getMessage());
            if (jwtException instanceof ExpiredJwtException) {
                log.debug("Expired JWT token: {}", token);
            } else if (jwtException instanceof MalformedJwtException) {
                log.debug("Malformed JWT token: {}", token);
            } else {
                log.debug("Other JWT error: {}", jwtException.getClass().getSimpleName());
            }
            return false;
        }
    }

}
