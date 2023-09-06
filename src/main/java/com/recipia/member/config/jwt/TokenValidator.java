package com.recipia.member.config.jwt;

import com.recipia.member.domain.Member;
import com.recipia.member.domain.constant.MemberStatus;
import com.recipia.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenValidator {

    private final MemberRepository memberRepository;

    public boolean isValidToken(String token, String tokenType) {
        try {
            Claims claims = TokenUtils.getClaimsFromToken(token);
            String type = claims.get("type", String.class);
            if (!type.equals(tokenType)) {
                return false;
            }
            String username = claims.get("username", String.class);
            Member member = memberRepository.findMemberByUsernameAndStatus(username, MemberStatus.ACTIVE).orElse(null);
            return member != null;
        } catch (JwtException jwtException) {
            return false;
        }
    }

}
