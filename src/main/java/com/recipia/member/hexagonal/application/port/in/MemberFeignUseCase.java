package com.recipia.member.hexagonal.application.port.in;

import com.recipia.member.hexagonal.adapter.in.feign.dto.NicknameDto;

public interface MemberFeignUseCase {

    NicknameDto getNicknameByMemberId(Long memberId);
}
