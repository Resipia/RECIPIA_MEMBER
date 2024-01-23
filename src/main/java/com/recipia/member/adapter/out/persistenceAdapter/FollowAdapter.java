package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.FollowEntity;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistenceAdapter.querydsl.FollowQueryRepository;
import com.recipia.member.application.port.out.port.FollowPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FollowAdapter implements FollowPort {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final FollowQueryRepository followQueryRepository;

    /**
     * [READ] 내가 팔로우 하고 있는 사람인지 검증
     * 팔로우 관계라면 true, 팔로우 관계가 없다면 false를 반환한다.
     */
    public boolean existsFollowRelation(Follow follow) {
        Optional<FollowEntity> optionalFollowEntity = followRepository.findFollowByFollowerMember_IdAndFollowingMember_Id(follow.getFollowerMemberId(), follow.getFollowingMemberId());
        return optionalFollowEntity.isPresent();
    }

    /**
     * [CREATE] 팔로우 저장
     * 팔로우를 저장하고 생성된 팔로우 pk값을 반환한다.
     *
     */
    @Override
    public Long follow(Follow follow) {
        MemberEntity follower = memberRepository.findMemberByIdAndStatus(follow.getFollowerMemberId(), MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        MemberEntity following = memberRepository.findMemberByIdAndStatus(follow.getFollowingMemberId(), MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        FollowEntity followEntity = FollowEntity.of(follower, following);
        FollowEntity savedFollow = followRepository.save(followEntity);
        return savedFollow.getId();
    }

    /**
     * [DELETE] 팔로우 삭제
     * 팔로우를 삭제하고 영향받은 데이터 갯수를 반환한다.
     */
    @Override
    public Long unfollow(Follow follow) {
        return followQueryRepository.deleteFollow(follow);
    }

}
