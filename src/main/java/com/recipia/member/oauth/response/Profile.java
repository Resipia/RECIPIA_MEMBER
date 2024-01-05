package com.recipia.member.oauth.response;

import java.util.Map;

/**
 * 프로필 정보
 */
public record Profile(String nickname) {

    public static Profile from(Map<String, Object> attributes) {
        return new Profile(
                String.valueOf(attributes.get("nickname"))
        );
    }

}
