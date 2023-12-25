package com.recipia.member.application.port.in;

import com.recipia.member.domain.Jwt;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public interface JwtUseCase {
    void insertRefreshTokenToDB(String email, Pair<String, LocalDateTime> jwtPair);
    String republishAccessToken(Jwt jwt);
}
