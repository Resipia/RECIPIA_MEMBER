package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Follow;

public interface FollowPort {
    boolean existsFollowRelation(Follow follow);
    Long follow(Follow follow);

    Long unfollow(Follow follow);
}
