package com.recipia.member.application.service;

import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.application.port.in.JwtUseCase;
import com.recipia.member.application.port.out.port.JwtPort;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.dto.TokenMemberInfoDto;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * JWT 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class JwtService implements JwtUseCase {

    private final MemberPort memberPort;
    private final JwtPort jwtPort;

    /**
     * [CREATE] refresh token db에 저장
     */
    @Transactional
    @Override
    public void insertRefreshTokenToDB(String email, Pair<String, LocalDateTime> jwtPair) {
        Member member = memberPort.findMemberByEmail(email).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        Jwt jwt = Jwt.of(member.getId(), jwtPair.getFirst(), jwtPair.getSecond());
        jwtPort.save(jwt);

    }

    /**
     * [READ] memberId, refresh token으로 access token 재발행
     */
    @Override
    public String republishAccessToken(Jwt jwt) {
        Jwt realJwt = jwtPort.getJwt(jwt);

        // db에서 가져온 refresh token 만료기한 체크
        boolean isRefreshTokenValid = Jwt.isRefreshTokenValid(LocalDateTime.now(), realJwt.getExpiredDateTime());

        if (!isRefreshTokenValid) {
            throw new MemberApplicationException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        // TokenMemberInfoDto에 넣어줄 회원 정보 가져오기
        Member member = memberPort.findMemberByIdAndStatus(jwt.getMemberId(), MemberStatus.ACTIVE);

        TokenMemberInfoDto tokenMemberInfoDto = TokenMemberInfoDto.of(member.getId(), member.getEmail(), null, member.getNickname(), member.getStatus(), member.getRoleType());

        // access token 생성 후 반환
        return TokenUtils.generateAccessToken(tokenMemberInfoDto);
    }

    /**
     * [READ] memberId로 저장된 데이터가 있는지 검증
     * 기존에 로그인된 회원(데이터 존재)이면 true, 로그인 되어있지 않은 회원(데이터 없음)이면 false 반환
     */
    @Override
    public boolean isLoggedIn(Long memberId) {
        return jwtPort.isLoggedIn(memberId);
    }
}
