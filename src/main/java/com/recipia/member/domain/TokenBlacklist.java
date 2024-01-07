package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TokenBlacklist {

    private Long id;
    private String token;
    private LocalDateTime expiredDateTime;

    private TokenBlacklist(Long id, String token, LocalDateTime expiredDateTime) {
        this.id = id;
        this.token = token;
        this.expiredDateTime = expiredDateTime;
    }

    public static TokenBlacklist of(String token, LocalDateTime expiredDateTime) {
        return new TokenBlacklist(null, token, expiredDateTime);
    }


}
