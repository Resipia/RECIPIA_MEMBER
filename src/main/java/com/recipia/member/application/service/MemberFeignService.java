package com.recipia.member.application.service;

import com.recipia.member.adapter.in.feign.dto.NicknameDto;
import com.recipia.member.application.port.in.MemberFeignUseCase;
import com.recipia.member.application.port.out.port.MemberFeignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberFeignService implements MemberFeignUseCase {

    private final MemberFeignPort memberFeignPort;

    public NicknameDto getNicknameByMemberId(Long memberId) {
        return memberFeignPort.getNicknameByMemberId(memberId);
    }
}
