package com.recipia.member.domain.converter;


import com.recipia.member.adapter.in.web.dto.request.JwtRepublishRequestDto;
import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.adapter.out.persistence.TokenBlacklistEntity;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.Logout;
import com.recipia.member.domain.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@Component
public class JwtConverter {

    public Jwt entityToDomain(JwtEntity entity) {
        return Jwt.of(entity.getId(), entity.getMemberId(), entity.getRefreshToken(), entity.getExpiredDateTime());
    }

    public JwtEntity domainToEntity(Jwt jwt) {
        return JwtEntity.of(jwt.getMemberId(), jwt.getRefreshToken(), jwt.getExpiredDateTime());
    }

    public Jwt requestDtoToDomain(JwtRepublishRequestDto requestDto) {
        return Jwt.of(null, requestDto.getMemberId(), requestDto.getRefreshToken(), null);
    }

    public TokenBlacklist logoutToTokenBlacklist(Logout logout) {
        // 현재 시간으로부터 30분 후의 시간을 계산
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(30);
        return TokenBlacklist.of(logout.getAccessToken(), expirationTime);
    }

    public TokenBlacklistEntity domainToEntity(TokenBlacklist domain) {
        return TokenBlacklistEntity.of(domain.getToken(), domain.getExpiredDateTime());
    }

    public TokenBlacklist entityToDomain(TokenBlacklistEntity entity) {
        return TokenBlacklist.of(entity.getId(), entity.getToken(), entity.getExpiredDateTime());
    }


}
