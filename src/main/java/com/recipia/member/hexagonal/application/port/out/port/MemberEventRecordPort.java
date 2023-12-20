package com.recipia.member.hexagonal.application.port.out.port;

public interface MemberEventRecordPort {

    long changePublishedToTrue(Long memberId, String topicName);
}
