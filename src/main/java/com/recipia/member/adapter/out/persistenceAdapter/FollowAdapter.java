package com.recipia.member.adapter.out.persistenceAdapter;

import com.recipia.member.adapter.out.persistence.FollowEntity;
import com.recipia.member.adapter.out.persistence.MemberEntity;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
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

    public boolean existsFollowRelation(Follow follow) {
        Optional<FollowEntity> optionalFollowEntity = followRepository.findFollowByFollowerMember_IdAndFollowingMember_Id(follow.getFollowerMemberId(), follow.getFollowingMemberId());
        return optionalFollowEntity.isPresent();
    }

    @Override
    public Long follow(Follow follow) {
        MemberEntity follower = memberRepository.findMemberByIdAndStatus(follow.getFollowerMemberId(), MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        MemberEntity following = memberRepository.findMemberByIdAndStatus(follow.getFollowingMemberId(), MemberStatus.ACTIVE).orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));
        FollowEntity followEntity = FollowEntity.of(follower, following);
        FollowEntity savedFollow = followRepository.save(followEntity);
        return savedFollow.getId();
    }

}
