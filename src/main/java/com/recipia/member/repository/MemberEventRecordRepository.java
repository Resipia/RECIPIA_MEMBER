package com.recipia.member.repository;

import com.recipia.member.domain.event.MemberEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEventRecordRepository extends JpaRepository<MemberEventRecord, Long> {

    /**
     * memberId, snsTopic에 해당하는 가장 최근 row 가져오기
     * @param memberId member pk
     * @param snsTopic snsTopic name
     */
    Optional<MemberEventRecord> findFirstByMember_IdAndSnsTopicOrderByIdDesc(Long memberId, String snsTopic);

}
