package com.recipia.member.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recipia.member.config.dto.SecurityUserDetailsDto;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.dto.MemberDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

/**
 * 사용자 인증 성공 후 처리하는 핸들러.
 * 사용자 상태에 따라 적절한 응답을 생성하고 JWT 토큰도 생성한다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 인증 성공 후 처리하는 메서드.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        // 사용자 정보 가져오기
        MemberDto memberDto = ((SecurityUserDetailsDto) authentication.getPrincipal()).getMemberDto();

        // 사용자 정보를 맵으로 변환
        HashMap<String, Object> memberDtoMap = objectMapper.readValue(objectMapper.writeValueAsString(memberDto), HashMap.class);

        HashMap<String, Object> responseMap = new HashMap<>();

        // 사용자 상태가 '휴먼 상태'인 경우
//        if (memberDto.status() == UserStatus.D) {
//            responseMap.put("userInfo", userDtoMap);
//            responseMap.put("resultCode", 9001);
//            responseMap.put("token", null);
//            responseMap.put("failMessage", "휴면 계정이야.");
//        }

        // 사용자 상태가 '휴먼 상태'가 아닌 경우
//        else {
            responseMap.put("memberInfo", memberDtoMap);
            responseMap.put("resultCode", 200);
            responseMap.put("failMessage", null);

            // JWT 토큰 생성
            String token = TokenUtils.generateJwtToken(memberDto);
            responseMap.put("token", token);

            // 쿠키에 JWT 토큰 저장
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);
//        }

        // 응답 설정하고 클라이언트에 전송
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()){
            printWriter.print(objectMapper.writeValueAsString(responseMap));
            printWriter.flush();
        }

    }
}
