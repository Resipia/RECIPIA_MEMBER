package com.recipia.member.hexagonal.adapter.in.feign;

import brave.Span;
import brave.Tracer;
import com.recipia.member.hexagonal.adapter.in.feign.dto.NicknameDto;
import com.recipia.member.hexagonal.application.service.MemberFeignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/feign/member")
@RestController
public class MemberFeignController {

    // fixme: service 의존성 말고 usecase 의존성 주입으로 바꿔야함
    private final MemberFeignService memberFeignService;
    private final Tracer tracer;

    @PostMapping("/getNickname")
    public NicknameDto getNickname(@RequestParam(name = "memberId") Long memberId) {
        Span span = tracer.nextSpan().name("[MEMBER] /feign/member/gitNickname received").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return memberFeignService.getNicknameByMemberId(memberId);
        } finally {
            span.finish();
        }
    }

}
