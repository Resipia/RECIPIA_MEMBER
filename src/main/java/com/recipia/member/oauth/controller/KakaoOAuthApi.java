package com.recipia.member.oauth.controller;

import com.recipia.member.oauth.response.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class KakaoOAuthApi {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;


    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    // 4번. 스프링부트가 kakao로 '인증 코드로 토큰 요청'
    public String getAccessToken(String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        // RestTemplate을 사용하여 카카오에 액세스 토큰 요청
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUri, params, Map.class);
        // 5번. kakao로부터 access token 전달 받음
        return (String) response.getBody().get("access_token");
    }


    public KakaoAccount getUserInfo(String accessToken) {
        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // RestTemplate을 사용하여 사용자 정보 요청
        ResponseEntity<KakaoAccount> response = restTemplate.exchange(userInfoUri, HttpMethod.POST, request, KakaoAccount.class);
        return response.getBody();
    }
}
