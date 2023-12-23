package com.recipia.member.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 사용자 인증이 실패했을 때 호출되는 핸들러.
 * 실패한 인증의 이유를 클라이언트에 JSON 형식으로 반환합니다.
 */
@Slf4j
@Component
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();  // Jackson 라이브러리의 ObjectMapper를 활용해 JSON 변환

    /**
     * 사용자 인증이 실패했을 때 호출되는 메서드.
     * 실패 이유를 파악한 후, 이를 JSON 형식으로 클라이언트에 반환한다.
     *
     * @param request  클라이언트로부터의 요청 정보
     * @param response 서버의 응답 정보
     * @param exception 인증 중 발생한 예외 정보
     * @throws IOException 입출력 예외가 발생한 경우
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        String failMessage = getFailureMessage(exception);

//        log.debug(failMessage);
        log.error(failMessage);

        resultMap.put("memberInfo", null);
        resultMap.put("resultCode", 9999);
        resultMap.put("failMessage", failMessage);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        try (PrintWriter printWriter = response.getWriter()) {
            printWriter.print(objectMapper.writeValueAsString(resultMap));
            printWriter.flush();
        }
    }

    /**
     * 인증 중 발생한 예외의 타입에 따라 실패 메시지를 반환합니다.
     *
     * @param exception 인증 중 발생한 예외 정보
     * @return 실패 메시지
     */
    private String getFailureMessage(AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            return "로그인 정보가 일치하지 않습니다.";
        } else if (exception instanceof LockedException) {
            return "계정이 잠겨 있습니다.";
        } else if (exception instanceof DisabledException) {
            return "계정이 비활성화되었습니다.";
        } else if (exception instanceof AccountExpiredException) {
            return "계정이 만료되었습니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            return "인증 정보가 만료되었습니다.";
        }
        return "알 수 없는 오류가 발생하였습니다.";
    }
}
