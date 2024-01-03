package com.recipia.member.application.port.in;

import com.recipia.member.domain.Follow;

public interface FollowUseCase {
    Long followRequest(Follow follow);
}
