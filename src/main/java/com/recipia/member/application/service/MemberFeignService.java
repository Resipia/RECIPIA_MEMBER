package com.recipia.member.application.service;

import com.recipia.member.adapter.in.feign.dto.NicknameDto;
import com.recipia.member.application.port.in.MemberFeignUseCase;
import com.recipia.member.application.port.out.port.MemberFeignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * feign 요청 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberFeignService implements MemberFeignUseCase {

    private final MemberFeignPort memberFeignPort;

    /**
     * [READ] memberId로 닉네임을 반환한다.
     */
    @Override
    public NicknameDto getNicknameByMemberId(Long memberId) {
        return memberFeignPort.getNicknameByMemberId(memberId);
    }
}
