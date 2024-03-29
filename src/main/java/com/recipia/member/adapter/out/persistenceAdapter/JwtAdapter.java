package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.JwtEntity;
import com.recipia.member.adapter.out.persistence.TokenBlacklistEntity;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.JwtQueryRepository;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.TokenBlacklist;
import com.recipia.member.domain.converter.JwtConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAdapter implements JwtPort {

    private final JwtRepository jwtRepository;
    private final JwtConverter jwtConverter;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final JwtQueryRepository jwtQueryRepository;

    /**
     * [CREATE] JWT refresh token 저장
     */
    @Override
    public void save(Jwt jwt) {
        jwtRepository.save(jwtConverter.domainToEntity(jwt));
    }

    /**
     * [READ] memberid, refreshToken으로 Jwt 가져오기
     */
    @Override
    public Jwt getJwt(Jwt jwt) {
        JwtEntity jwtEntity = jwtRepository.findJwtByMemberIdAndRefreshToken(jwt.getMemberId(), jwt.getRefreshToken()).orElseThrow(() -> new MemberApplicationException(ErrorCode.JWT_NOT_FOUND));
        return jwtConverter.entityToDomain(jwtEntity);
    }

    /**
     * [DELETE] memberId로 JWT 삭제
     */
    @Override
    public void deleteRefreshToken(Long memberId) {
        jwtRepository.deleteByMemberId(memberId);
    }

    /**
     * [CREATE] token blacklist 저장
     */
    @Override
    public void insertTokenBlacklist(TokenBlacklist tokenBlacklist) {
        TokenBlacklistEntity tokenBlacklistEntity = jwtConverter.domainToEntity(tokenBlacklist);
        tokenBlacklistRepository.save(tokenBlacklistEntity);
    }

    /**
     * [READ] token으로 tokenblacklist 조회
     * 존재하면 도메인 반환, 없으면 null 반환
     */
    @Override
    public TokenBlacklist getTokenBlacklist(TokenBlacklist tokenBlacklist) {
        Optional<TokenBlacklistEntity> optionalTokenBlacklistEntity = tokenBlacklistRepository.findByToken(tokenBlacklist.getToken());
        return optionalTokenBlacklistEntity.map(jwtConverter::entityToDomain).orElse(null);
    }

    /**
     * [READ] memberId로 데이터 검색
     * 기존에 저장된 데이터가 있으면 true, 없으면 false 반환
     */
    @Override
    public boolean isLoggedIn(Long memberId) {
        return jwtRepository.existsByMemberId(memberId);
    }

    /**
     * [DELETE] email로 회원을 찾아서 그 회원의 JWT를 삭제한다.
     */
    @Override
    public void deleteRefreshTokenByEmail(String email) {
        jwtQueryRepository.deleteByMemberEmail(email);
    }

}
