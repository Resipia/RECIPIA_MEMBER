package com.recipia.member.dto.message;

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
