package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    Optional<FollowEntity> findFollowByFollowerMember_IdAndFollowingMember_Id(Long followerId, Long FollowingId);

    List<FollowEntity> findAllByFollowingMember_Id(Long memberId);

    List<FollowEntity> findAllByFollowerMember_Id(Long memberId);
}
