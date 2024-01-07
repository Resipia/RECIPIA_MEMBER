package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.adapter.out.persistence.TokenBlacklistEntity;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.TokenBlacklist;
import com.recipia.member.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtAdapter implements JwtPort {

    private final JwtRepository jwtRepository;
    private final JwtConverter jwtConverter;
    private final TokenBlacklistRepository tokenBlacklistRepository;

    @Override
    public void save(Jwt jwt) {
        jwtRepository.save(jwtConverter.domainToEntity(jwt));
    }

    /**
     * jwt의 memberid, refreshToken으로 Jwt 가져오기
     */
    @Override
    public Jwt getJwt(Jwt jwt) {
        JwtEntity jwtEntity = jwtRepository.findJwtByMemberIdAndRefreshToken(jwt.getMemberId(), jwt.getRefreshToken()).orElseThrow(() -> new MemberApplicationException(ErrorCode.JWT_NOT_FOUND));
        return jwtConverter.entityToDomain(jwtEntity);
    }

    @Override
    public void deleteRefreshToken(Long memberId) {
        jwtRepository.deleteById(memberId);
    }

    @Override
    public void insertTokenBlacklist(TokenBlacklist tokenBlacklist) {
        TokenBlacklistEntity tokenBlacklistEntity = jwtConverter.domainToEntity(tokenBlacklist);
        tokenBlacklistRepository.save(tokenBlacklistEntity);
    }

}
