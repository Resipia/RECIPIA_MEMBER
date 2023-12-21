package com.recipia.member.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recipia.member.application.port.in.JwtUseCase;
import com.recipia.member.config.dto.SecurityUserDetailsDto;
import com.recipia.member.config.dto.TokenMemberInfoDto;
import com.recipia.member.config.jwt.TokenUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
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
    private final JwtUseCase jwtUseCase;

    /**
     * 인증 성공 후 처리하는 메서드.
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        // 사용자 정보 가져오기
        TokenMemberInfoDto tokenMemberInfoDto = ((SecurityUserDetailsDto) authentication.getPrincipal()).getTokenMemberInfoDto();

        // JWT 토큰 생성
        String accessToken = TokenUtils.generateAccessToken(tokenMemberInfoDto);
        Pair<String, LocalDateTime> refreshTokenPair = TokenUtils.generateRefreshToken(tokenMemberInfoDto);

        // Refresh Token DB에 저장
        jwtUseCase.insertRefreshTokenToDB(tokenMemberInfoDto.email(), refreshTokenPair);

        // 응답 맵 생성 및 토큰 추가
        HashMap<String, Object> responseMap = new HashMap<>();
        responseMap.put("resultCode", 200);
        responseMap.put("accessToken", accessToken);
        responseMap.put("refreshToken", refreshTokenPair.getFirst());

        // 응답 설정
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        // 응답 데이터 클라이언트에 전송
        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(objectMapper.writeValueAsString(responseMap));
            printWriter.flush();
        }

    }
}
