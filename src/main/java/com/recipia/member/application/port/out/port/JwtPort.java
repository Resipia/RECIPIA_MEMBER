package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.TokenBlacklist;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface JwtPort {
    void save(Jwt jwt);
    Jwt getJwt(Jwt jwt);
    void deleteRefreshToken(Long memberId);

    void insertTokenBlacklist(TokenBlacklist tokenBlacklist);

    TokenBlacklist getTokenBlacklist(TokenBlacklist tokenBlacklist);
}
