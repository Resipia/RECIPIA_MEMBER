package com.recipia.member.application.port.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipia.member.domain.MemberEventRecord;

public interface MemberEventRecordUseCase {
    void changePublishedToTrue(Long memberId, String topicName);
    void saveNewEventRecord(MemberEventRecord memberEventRecord) throws JsonProcessingException;
}
