package com.recipia.member.oauth.response;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * 이 필드들은 Kakao developer 문서에 적혀있는 Response를 그대로 가져와서 만들어준 것이다.
 */
// TODO: Map -> Object 변환 로직이 있어 제네릭 타입 캐스팅 문제를 무시한다.
@SuppressWarnings("unchecked")
public record KakaoOAuth2Response(
        Long id,
        LocalDateTime connectedAt,
        Map<String, Object> properties,
        KakaoAccount kakaoAccount
) {


    // attributes를 받아서 Response로 만들어주는 factory 메소드를 정의한다.
    public static KakaoOAuth2Response from(Map<String, Object> attributes) {

        // attributes에서 값을 꺼내 KakaoOAuth2Response의 생성자를 통해 값을 세팅하여 KakaoOAuth2Response 인스턴스 객체를 생성해준다.
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),                      // id 세팅
                LocalDateTime.parse(                                                     // 연결시간 세팅
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())   // ISO_INSTANT로 변환을 해줘야 한다.
                ),
                (Map<String, Object>) attributes.get("properties"),                      // properties 세팅
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account")) // KakaoAccount 세팅.
        );
    }

    // 잘 사용될 이메일 값을 getter로 만들었다.
    public String email() {
        return this.kakaoAccount().email();
    }

    /**
     * properties에도 nickname이 존재하지만 그것 말고 KakaoAccount()내부의 nickname을 사용해야 한다.
     * properties.nickname -> deprecated 되었다.
     */
    public String nickname() {
        return this.kakaoAccount().nickname();
    }

}
