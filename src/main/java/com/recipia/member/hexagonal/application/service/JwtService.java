package com.recipia.member.hexagonal.application.service;

import com.recipia.member.hexagonal.adapter.out.persistence.JwtEntity;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.JwtRepository;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// fixme: usecase 추가
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class JwtService {

    private final MemberRepository memberRepository;
    private final JwtRepository jwtRepository;

    /**
     * refresh token db에 저장
     */
    @Transactional
    public void insertRefreshTokenToDB(String username, Pair<String, LocalDateTime> jwtPair) {
        MemberEntity member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        JwtEntity jwtEntity = JwtEntity.of(member, jwtPair.getFirst(), jwtPair.getSecond());
        jwtRepository.save(jwtEntity);
    }
}
