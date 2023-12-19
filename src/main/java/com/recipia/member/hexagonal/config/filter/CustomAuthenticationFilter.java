package com.recipia.member.hexagonal.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.recipia.member.hexagonal.common.exception.ErrorCode;
import com.recipia.member.hexagonal.common.exception.MemberApplicationException;
import com.recipia.member.hexagonal.config.dto.TokenMemberInfoDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // ObjectMapper는 스레드에 안전하여 한 번만 생성하고 재사용하는 것이 효율적이다.
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * 생성자. AuthenticationManager를 인자로 받아 상위 클래스에 전달한다.
     * @param authenticationManager 스프링 시큐리티의 인증을 처리하는 관리 객체
     */
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 이 메서드는 사용자가 로그인을 시도할 때 호출된다.
     * HTTP 요청에서 사용자 이름과 비밀번호를 추출하여 UsernamePasswordAuthenticationToken 객체를 생성하고, 이를 AuthenticationManager에 전달하여 인증을 시도한다.
     * 인증이 성공하면 인증된 사용자의 정보와 권한을 담은 Authentication 객체를 반환하고, 인증이 실패하면 AuthenticationException을 던진다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UsernamePasswordAuthenticationToken authRequest;

        try {
            authRequest = getAuthRequest(request);
            // Authentication 객체에 authRequest 설정
            setDetails(request, authRequest);
        } catch (Exception e) {
            throw new MemberApplicationException(ErrorCode.FILTER_USERNAME_PASSWORD_AUTHENTICATION_TOKEN);
        }

        // Authentication 객체 반환
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    /**
     * HttpServletRequest에서 TokenMemberInfoDto에 관련된 데이터 추출하기
     */
    private UsernamePasswordAuthenticationToken getAuthRequest (HttpServletRequest request) {
        try {
            TokenMemberInfoDto tokenMemberInfoDto = objectMapper.readValue(request.getInputStream(), TokenMemberInfoDto.class);
            return new UsernamePasswordAuthenticationToken(tokenMemberInfoDto.username(), tokenMemberInfoDto.password());
        } catch (Exception e) {
            throw new MemberApplicationException(ErrorCode.IO_ERROR);
        }
    }
}
