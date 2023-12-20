package com.recipia.member.hexagonal.application.port.in;

public interface MemberEventRecordUseCase {
    void changePublishedToTrue(Long memberId, String topicName);
}
