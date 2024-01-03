package com.recipia.member.domain.converter;


import com.recipia.member.adapter.in.web.dto.request.JwtRepublishRequestDto;
import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.domain.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        return JwtEntity.of(jwt.getId(), jwt.getMemberId(), jwt.getRefreshToken(), jwt.getExpiredDateTime());
    }

    public Jwt requestDtoToDomain(JwtRepublishRequestDto requestDto) {
        return Jwt.of(null, requestDto.getMemberId(), requestDto.getRefreshToken(), null);

    }

}
