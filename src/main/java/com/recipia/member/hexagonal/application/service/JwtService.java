package com.recipia.member.hexagonal.application.service;

import com.recipia.member.hexagonal.adapter.out.persistence.JwtEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.application.port.in.JwtUseCase;
import com.recipia.member.hexagonal.application.port.out.port.JwtPort;
import com.recipia.member.hexagonal.application.port.out.port.MemberPort;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.JwtRepository;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import com.recipia.member.hexagonal.domain.Jwt;
import com.recipia.member.hexagonal.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class JwtService implements JwtUseCase {

    private final MemberPort memberPort;
    private final JwtPort jwtPort;

    /**
     * refresh token db에 저장
     */
    @Transactional
    public void insertRefreshTokenToDB(String email, Pair<String, LocalDateTime> jwtPair) {
        Member member = memberPort.findMemberByEmail(email).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        Jwt jwt = Jwt.of(member.getId(), jwtPair.getFirst(), jwtPair.getSecond());
        jwtPort.save(jwt);

    }
}
