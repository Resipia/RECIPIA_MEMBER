package com.recipia.member.adapter.in.listener.aws.dto;

/**
 * SNS messageAttribute 담을 dto
 * @param traceId
 */
public record MessageAttributesDto(
        TraceIdDto traceId
) {
}
