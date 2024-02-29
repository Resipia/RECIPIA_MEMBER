package com.recipia.member.adapter.in.listener.aws.dto;

/**
 * SNS MessageAttribute에 담겨있는 TraceId
 */
public record TraceIdDto(
        String Type,
        String Value
) {
}
