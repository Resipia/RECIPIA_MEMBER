package com.recipia.member.dto;

public record SnsInformationDto(
        String Type,
        String MessageId,
        String TopicArn,
        String Message, // 이 필드는 JSON 문자열이므로 나중에 MemberMessageDto로 파싱됩니다.
        String Timestamp,
        String SignatureVersion,
        String Signature,
        String SigningCertURL,
        String UnsubscribeURL
) {
}
