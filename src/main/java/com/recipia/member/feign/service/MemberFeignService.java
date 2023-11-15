package com.recipia.member.feign.service;

import com.recipia.member.domain.Member;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.feign.dto.NicknameDto;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberFeignService {

    private final MemberRepository memberRepository;


    public NicknameDto getNicknameByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberApplicationException(ErrorCode.DB_ERROR));
        return NicknameDto.of(member.getId(), member.getNickname());
    }
}
