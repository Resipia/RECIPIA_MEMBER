package com.recipia.member.service;

import com.recipia.member.dto.MemberDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberDto findMember(Long userId) {
        return memberRepository.findById(userId)
                .map(MemberDto::fromEntity)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
    }

}
