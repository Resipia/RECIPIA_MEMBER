package com.recipia.member.application.port.in;

public interface MemberEventRecordUseCase {
    void changePublishedToTrue(Long memberId, String topicName);
}
