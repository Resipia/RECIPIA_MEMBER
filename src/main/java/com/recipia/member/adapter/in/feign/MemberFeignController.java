package com.recipia.member.adapter.in.feign;

import brave.Span;
import brave.Tracer;
import com.recipia.member.adapter.in.feign.dto.NicknameDto;
import com.recipia.member.application.port.in.MemberFeignUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * feign 요청에 응답하는 feign 전용 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/feign/member")
@RestController
public class MemberFeignController {

    private final MemberFeignUseCase memberFeignUseCase;
    private final Tracer tracer;

    /**
     * 닉네임 조회
     */
    @PostMapping("/getNickname")
    public NicknameDto getNickname(@RequestParam(name = "memberId") Long memberId) {
        Span span = tracer.nextSpan().name("[MEMBER] /feign/member/gitNickname received").start();
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span)) {
            return memberFeignUseCase.getNicknameByMemberId(memberId);
        } finally {
            span.finish();
        }
    }

}
