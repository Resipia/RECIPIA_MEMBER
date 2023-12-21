package com.recipia.member.adapter.in.listener.aws.dto;

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
