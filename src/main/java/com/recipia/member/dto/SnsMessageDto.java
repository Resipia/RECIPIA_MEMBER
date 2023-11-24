package com.recipia.member.dto;

public record SnsMessageDto(String traceId, Long memberId) {


    public static SnsMessageDto of(String traceId, Long memberId) {
        return new SnsMessageDto(traceId, memberId);
    }
}
