package com.recipia.member.repository;

import com.recipia.member.domain.event.MemberEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEventRecordRepository extends JpaRepository<MemberEventRecord, Long> {
}
