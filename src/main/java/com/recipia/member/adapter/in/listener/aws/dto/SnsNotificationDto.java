package com.recipia.member.adapter.in.listener.aws.dto;

/**
 * SNS에 담겨서 들어오는 데이터
 */
public record SnsNotificationDto(
        String Type,
        String MessageId,
        String TopicArn,
        String Message,
        String Timestamp,
        String SignatureVersion,
        String Signature,
        String SigningCertURL,
        String UnsubscribeURL,
        MessageAttributesDto MessageAttributes
) {
}
