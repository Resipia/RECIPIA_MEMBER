package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.FollowEntity;
import com.recipia.member.adapter.out.persistenceAdapter.FollowRepository;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("[통합] Follow query repository 테스트")
@Transactional
class FollowQueryRepositoryTest extends TotalTestSupport {

    @Autowired
    private FollowQueryRepository sut;
    @Autowired
    private FollowRepository followRepository;

    @DisplayName("[happy] 팔로우 데이터를 삭제한다.")
    @Test
    void unfollowSuccess() {
        // given
        Follow follow = Follow.of(1L, 2L);
        // when
        sut.deleteFollow(follow);
        // then
        Optional<FollowEntity> optionalFollowEntity = followRepository.findFollowByFollowerMember_IdAndFollowingMember_Id(1L, 2L);
        assertThat(optionalFollowEntity).isEmpty();
    }

    @DisplayName("[happy] 나를 팔로우하는 관계를 삭제한다.")
    @Test
    void deleteFollowingByMemberId() {
        // given
        Long memberId = 1L;
        // when
        sut.deleteFollowingByMemberId(memberId);
        // then
        List<FollowEntity> allByFollowingMemberId = followRepository.findAllByFollowingMember_Id(memberId);
        assertTrue(allByFollowingMemberId.isEmpty());
    }

    @DisplayName("[happy] 내가 팔로우하는 관계를 삭제한다.")
    @Test
    void deleteFollowerByMemberId() {
        // given
        Long memberId = 1L;
        // when
        sut.deleteFollowerByMemberId(memberId);
        // then
        List<FollowEntity> allByFollowerMemberId = followRepository.findAllByFollowerMember_Id(memberId);
        assertTrue(allByFollowerMemberId.isEmpty());
    }
}