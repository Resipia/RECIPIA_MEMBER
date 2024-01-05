package com.recipia.member.oauth.service;

import com.recipia.member.application.port.out.port.SignUpPort;
import com.recipia.member.oauth.response.KakaoOAuth2Response;
import com.recipia.member.oauth.response.OAuthPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * OAuth2 전용 서비스 클래스
 * OAuth 2.0 기술을 사용한 사용자 인증을 위한 메소드다. OAuth2UserService -> @FunctionalInterface 라서 람다식으로 작성이 가능하다.
 * 여기가 매우 중요하다! 핵심 기능이다. 이 코드는 userDetailsService와 같은 역할을 하는 녀석이다.
 */
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SignUpPort signUpPort;

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1. 람다식의 입력을 넣어주고 user정보를 가져온다.
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2. oAuth2User로부터 데이터를 Map<>으로 받아서 내가만든 팩토리 메서드의 값을 넣어줘서 response 데이터를 만든다.
        KakaoOAuth2Response kakaoResponse = KakaoOAuth2Response.from(oAuth2User.getAttributes());
        
        // 3. 이것은 고유값이다. registrationId를 뽑아낸다. yml파일의 client:registration:kakao 에서 [kakao]이다.-> [provider의 id]
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 4. 우리가 만든 response에서 Id를 가져온다.
        String providerId = String.valueOf(kakaoResponse.id());

        // 5. 최종적으로 위에서 만든 값을 가지고 [kakao_{고유값}] 이렇게 username을 만들어줘서 user를 검색하고 없으면 회원가입을 시켜준다.
        String email = registrationId + "_" + providerId;

        // 6. 비밀번호도 만들어준다. -> 카카오 로그인에는 필요없지만 필드자체를 not null로 설계해버려서 가짜를 만들어준다. bcrypt로 인코딩해서 넣는다.
        String dummyPassword = passwordEncoder.encode("{bcrypt}" + UUID.randomUUID());

        // 7. return으로 db에 유저가 있다면 ok , db에 유저가 없다면 가입을 시킨다.
        //todo: id를 이메일로
        //todo: 이미 우리 db 멤버에서 찾는지 아니면 새로운 oauth에서 찾는지 알아보기
        OAuth2User oAuthPrincipal = signUpPort.findById(email)
                .map(OAuthPrincipal::fromDto)
                .orElseGet(() ->
                        OAuthPrincipal.fromDto(
                                signUpPort.saveUser(
                                        kakaoResponse.email(), // 이메일
                                        dummyPassword, // 비밀번호
                                        kakaoResponse.nickname(), // 닉네임
                                        null // 한줄소개
                                )
                        )
                );
        return oAuthPrincipal;
        // 8 todo: jwt 발급



    }
}
