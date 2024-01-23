package com.recipia.member.application.service;

import com.recipia.member.application.port.in.FollowUseCase;
import com.recipia.member.application.port.out.port.FollowPort;
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
     * [CREATE/DELETE]
     * 이미 팔로우 관계가 있는 두 회원이면 팔로우 해제 프로세스를 진행하고
     * 팔로우 관계가 없다면 팔로우 생성 프로세스를 진행한다.
     */
    @Transactional
    @Override
    public Long followRequest(Follow follow) {
        // 이미 팔로우 관계인지 검증
        boolean alreadyFollowing = followPort.existsFollowRelation(follow);

        if (alreadyFollowing) {
            // 팔로우 관계라면 팔로우 해제
            return followPort.unfollow(follow);
        } else {
            // 팔로우 관계가 없다면 팔로우 요청
            return followPort.follow(follow);
        }
    }
}
