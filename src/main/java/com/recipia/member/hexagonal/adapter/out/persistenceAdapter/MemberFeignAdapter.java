package com.recipia.member.hexagonal.adapter.out.persistenceAdapter;

import com.recipia.member.hexagonal.adapter.in.feign.dto.NicknameDto;
import com.recipia.member.hexagonal.adapter.out.persistence.MemberEntity;
import com.recipia.member.hexagonal.application.port.out.port.MemberFeignPort;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberFeignAdapter implements MemberFeignPort {

    private final MemberRepository memberRepository;

    public NicknameDto getNicknameByMemberId(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        return NicknameDto.of(member.getId(), member.getNickname());
    }

}
