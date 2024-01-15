package com.recipia.member.application.service;

import com.recipia.member.application.port.in.FollowUseCase;
import com.recipia.member.application.port.out.port.FollowPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Follow;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 팔로우 서비스 클래스
 */
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService implements FollowUseCase {

    private final FollowPort followPort;

    /**
     * [CREATE] 팔로우를 생성한다.
     */
    @Transactional
    @Override
    public Long followRequest(Follow follow) {
        // 1. 이미 팔로우,팔로잉 관계인지 확인
        boolean alreadyFollowing = followPort.existsFollowRelation(follow);
        if (alreadyFollowing) {
            throw new MemberApplicationException(ErrorCode.ALREADY_FOLLOWING);
        }
        // 팔로우 관계가 아니라면 팔로우 생성
        return followPort.follow(follow);
    }
}
