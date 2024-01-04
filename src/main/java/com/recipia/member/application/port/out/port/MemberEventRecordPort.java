package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.MemberEventRecord;

public interface MemberEventRecordPort {

    Long changePublishedToTrue(Long memberId, String topicName);
    Long changeBeforeEventAllPublishedToTrue(Long memberId, String topicName);
    void save(MemberEventRecord memberEventRecordNew);
}
