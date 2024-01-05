package com.recipia.member.oauth.controller;

import com.recipia.member.oauth.response.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class KakaoOAuthApi {

//    @Value("${kakao.client-id}")
//    private String clientId;
//
//    @Value("${kakao.redirect-uri}")
//    private String redirectUri;
//
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public String getAccessToken(String code) {
//        String tokenUri = "https://kauth.kakao.com/oauth/token";
//        Map<String, String> params = new HashMap<>();
//        params.put("code", code);
//        params.put("client_id", clientId);
//        params.put("redirect_uri", redirectUri);
//        params.put("grant_type", "authorization_code");
//
//        // RestTemplate을 사용하여 카카오에 액세스 토큰 요청
//        Map<String, String> response = restTemplate.postForObject(tokenUri, params, Map.class);
//        return response.get("access_token");
//    }
//
//    public KakaoAccount getUserInfo(String accessToken) {
//        String userInfoUri = "https://kapi.kakao.com/v2/user/me";
//        Map<String, String> headers = new HashMap<>();
//        headers.put("Authorization", "Bearer " + accessToken);
//
//        // RestTemplate을 사용하여 사용자 정보 요청
//        KakaoAccount userInfo = restTemplate.postForObject(userInfoUri, headers, KakaoAccount.class);
//        return userInfo;
//    }
}
