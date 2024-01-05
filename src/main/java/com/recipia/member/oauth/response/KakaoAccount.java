package com.recipia.member.oauth.response;

import java.util.Map;

/**
 * 카카오 계정 정보
 */
public record KakaoAccount(
        Boolean profileNicknameNeedsAgreement,
        Profile profile,
        Boolean hasEmail,
        Boolean emailNeedsAgreement,
        Boolean isEmailValid,
        Boolean isEmailVerified,
        String email
) {

    /**
     * attributes를 받아서 KakaoAccount를 만들어주는 factory 메소드
     * String.valueOf()는 좀 더 섬세한 처리가 되어있다. -> null safe한 로직이 추가되어 있다.
     */
    public static KakaoAccount from(Map<String, Object> attributes) {
        return new KakaoAccount(
                Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                Profile.from((Map<String, Object>) attributes.get("profile")),
                Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                String.valueOf(attributes.get("email"))
        );
    }

    public String nickname() { return this.profile().nickname(); }
}
