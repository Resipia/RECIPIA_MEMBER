package com.recipia.member.feign.controller;

import com.recipia.member.feign.dto.NicknameDto;
import com.recipia.member.feign.service.MemberFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/feign/member")
@RestController
public class MemberFeignController {

    private final MemberFeignService memberFeignService;

    @PostMapping("/getNickname")
    public NicknameDto getNickname(Long memberId) {
        return memberFeignService.getNicknameByMemberId(memberId);
    }

}
