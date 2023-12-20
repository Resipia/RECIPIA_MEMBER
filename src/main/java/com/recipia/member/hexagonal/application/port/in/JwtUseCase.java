package com.recipia.member.hexagonal.application.port.in;

import org.springframework.data.util.Pair;

import java.time.LocalDateTime;

public interface JwtUseCase {
    void insertRefreshTokenToDB(String email, Pair<String, LocalDateTime> jwtPair);
}
