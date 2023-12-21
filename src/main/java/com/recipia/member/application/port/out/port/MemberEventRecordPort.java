package com.recipia.member.application.port.out.port;

public interface MemberEventRecordPort {

    long changePublishedToTrue(Long memberId, String topicName);
}
