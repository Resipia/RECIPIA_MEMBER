package com.recipia.member.oauth.controller;

import com.recipia.member.oauth.response.KakaoAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class KakaoLoginController {

//    private final KakaoOAuthApi kakaoOAuthApi;
//
//    @GetMapping("/kakao/callback")
//    public ResponseEntity<?> kakaoLoginCallback(@RequestParam("code") String code) {
//        String accessToken = kakaoOAuthApi.getAccessToken(code);
//        if (accessToken == null || accessToken.isEmpty()) {
//            return ResponseEntity.badRequest().body("Failed to get access token");
//        }
//
//        KakaoAccount userInfo = kakaoOAuthApi.getUserInfo(accessToken);
//        if (userInfo == null) {
//            return ResponseEntity.badRequest().body("Failed to get user information");
//        }
//
//        // 여기서 사용자 정보를 바탕으로 로직 처리
//        return ResponseEntity.ok(userInfo);
//    }
}
