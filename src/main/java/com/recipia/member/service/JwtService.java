package com.recipia.member.service;

import com.recipia.member.domain.Jwt;
import com.recipia.member.domain.Member;
import com.recipia.member.dto.MemberDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.JwtRepository;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
    public void insertRefreshTokenToDB(MemberDto memberDto, Pair<String, LocalDateTime> jwtPair) {
        Member member = memberRepository.findMemberByUsername(memberDto.username()).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        Jwt jwt = Jwt.of(member, jwtPair.getFirst(), jwtPair.getSecond());
        jwtRepository.save(jwt);
    }
}
