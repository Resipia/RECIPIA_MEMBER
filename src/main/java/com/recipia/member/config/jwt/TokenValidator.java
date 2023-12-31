package com.recipia.member.config.jwt;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
            return false;
        }
    }

}
