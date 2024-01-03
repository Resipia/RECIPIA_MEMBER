package com.recipia.member.application.port.out.port;

import com.recipia.member.domain.Follow;
import org.reactivestreams.Publisher;

public interface FollowPort {
    boolean existsFollowRelation(Follow follow);
    Long follow(Follow follow);
}
