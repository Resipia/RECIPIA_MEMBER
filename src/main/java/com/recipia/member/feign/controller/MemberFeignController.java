package com.recipia.member.feign.controller;

import brave.Span;
import brave.Tracer;
import com.recipia.member.feign.dto.NicknameDto;
import com.recipia.member.feign.service.MemberFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/feign/member")
@RestController
public class MemberFeignController {

    private final MemberFeignService memberFeignService;
    private final Tracer tracer;

    @PostMapping("/getNickname")
    public NicknameDto getNickname(@RequestParam(name = "memberId") Long memberId) {
        Span span = tracer.nextSpan().name("Feign Get Nickname").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return memberFeignService.getNicknameByMemberId(memberId);
        } finally {
            span.finish();
        }
    }

}
