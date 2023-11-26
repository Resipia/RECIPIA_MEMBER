package com.recipia.member.repository;

import com.recipia.member.domain.event.MemberEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEventRecordRepository extends JpaRepository<MemberEventRecord, Long> {

    /**
     * published = false인 데이터중에서
     * memberId, snsTopic에 해당하는 가장 최근 row 가져오기
     * @param memberId memberId
     * @param snsTopic sns topic 명
     * @param published published=false
     */
    Optional<MemberEventRecord> findFirstByMember_IdAndSnsTopicAndPublishedOrderByIdDesc(Long memberId, String snsTopic, boolean published);

}
