package com.recipia.member.adapter.out.persistenceAdapter.querydsl;

import com.recipia.member.adapter.out.persistence.FollowEntity;
import com.recipia.member.adapter.out.persistenceAdapter.FollowRepository;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

}