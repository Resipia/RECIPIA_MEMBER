package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.TokenBlacklist;
import org.springframework.stereotype.Component;

public interface JwtPort {
    // refresh token 저장용
    void save(Jwt jwt);
    // memberid, refresh token으로 Jwt 가져오기
    Jwt getJwt(Jwt jwt);
    void deleteRefreshToken(Long memberId);

    void insertTokenBlacklist(TokenBlacklist tokenBlacklist);
}
