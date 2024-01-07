package com.recipia.member.oauth.response;

import java.util.Map;

/**
 * 카카오 계정 정보
 */
public record KakaoAccount(
        String nickname,
        String profileImage,
        String email,
        String name,
        String phoneNumber

//        Boolean profileNicknameNeedsAgreement,
//        Profile profile,
//        Boolean hasEmail,
//        Boolean emailNeedsAgreement,
//        Boolean isEmailValid,
//        Boolean isEmailVerified,
//        String email,
) {

    /**
     * attributes를 받아서 KakaoAccount를 만들어주는 factory 메소드
     * String.valueOf()는 좀 더 섬세한 처리가 되어있다. -> null safe한 로직이 추가되어 있다.
     */
    public static KakaoAccount from(Map<String, Object> attributes) {
        return new KakaoAccount(
                String.valueOf(attributes.get("profile_nickname")),
                String.valueOf(attributes.get("profile_image")),
                String.valueOf(attributes.get("account_email")),
                String.valueOf(attributes.get("name")),
                String.valueOf(attributes.get("phone_number"))
        );
    }

//    public String nickname() { return this.profile().nickname(); }
}
