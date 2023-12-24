package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAdapter implements JwtPort {

    private final JwtRepository jwtRepository;

    @Override
    public void save(Jwt jwt) {
        jwtRepository.save(JwtConverter.domainToEntity(jwt));
    }

    /**
     * jwt의 memberid, refreshToken으로 Jwt 가져오기
     */
    @Override
    public Jwt getJwt(Jwt jwt) {
        JwtEntity jwtEntity = jwtRepository.findJwtByMemberIdAndRefreshToken(jwt.getMemberId(), jwt.getRefreshToken()).orElseThrow(() -> new MemberApplicationException(ErrorCode.JWT_NOT_FOUND));
        return JwtConverter.entityToDomain(jwtEntity);
    }

}
