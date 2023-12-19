package com.recipia.member.hexagonal.domain.converter;


import com.recipia.member.hexagonal.adapter.out.persistence.JwtEntity;
import com.recipia.member.hexagonal.domain.Jwt;
import lombok.RequiredArgsConstructor;

/**
 * dto, entity와 domain을 변환해주는 로직과
 * domain을 entity로 변환해주는 로직을 담당
 */
@RequiredArgsConstructor
public class JwtConverter {

    public static Jwt entityToDomain(JwtEntity entity) {
        return Jwt.of(entity.getId(), entity.getMemberId(), entity.getRefreshToken(), entity.getExpiredDateTime());
    }

    public static JwtEntity domainToEntity(Jwt jwt) {
        return JwtEntity.of(jwt.getId(), jwt.getMemberId(), jwt.getRefreshToken(), jwt.getExpiredDateTime());
    }

}
