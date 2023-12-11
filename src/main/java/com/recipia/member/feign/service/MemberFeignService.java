package com.recipia.member.feign.service;

import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.feign.dto.NicknameDto;
import com.recipia.member.hexagonal.adapter.out.persistenceAdapter.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberFeignService {

    private final MemberRepository memberRepository;


    public NicknameDto getNicknameByMemberId(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return NicknameDto.of(member.getId(), member.getNickname());
    }
}
