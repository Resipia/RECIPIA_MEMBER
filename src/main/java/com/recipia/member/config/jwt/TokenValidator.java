package com.recipia.member.config.jwt;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.MemberEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.entity.constant.MemberStatus;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
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
            // 1. claims에서 type을 추출한다.
            Claims claims = TokenUtils.getClaimsFromToken(token);
            String type = claims.get("type", String.class);

            // 2. 토큰 타입을 확인한다. ex) access
            if (!type.equals(tokenType)) {
                return false;
            }

            //todo: 지금 claims안에 username이 없어서 검증오류가 생긴것이다.
            String username = claims.get("username", String.class);
            MemberEntity member = memberRepository.findMemberByUsernameAndStatus(username, MemberStatus.ACTIVE).orElse(null);

            return member != null;
        } catch (JwtException jwtException) {
            return false;
        }
    }

}
