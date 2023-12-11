package com.recipia.member.repository;

import com.recipia.member.hexagonal.adapter.out.persistence.entity.MemberEventRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberEventRecordRepository extends JpaRepository<MemberEventRecordEntity, Long> {

    /**
     * published = false인 데이터중에서
     * memberId, snsTopic에 해당하는 가장 최근 row 가져오기
     * @param memberId memberId
     * @param snsTopic sns topic 명
     * @param published published=false
     */
    Optional<MemberEventRecordEntity> findFirstByMember_IdAndSnsTopicAndPublishedOrderByIdDesc(Long memberId, String snsTopic, boolean published);

    /**
     * snsTopic에 해당하는 memberId의 사용자가 동일한 sns 메시지를 발행하면 기존에 false로 있던 데이터 가져오기
     * @param memberId memberId
     * @param snsTopic snsTopic
     * @param published published=false
     */
    List<MemberEventRecordEntity> findByMember_IdAndSnsTopicAndPublished(Long memberId, String snsTopic, boolean published);


}
