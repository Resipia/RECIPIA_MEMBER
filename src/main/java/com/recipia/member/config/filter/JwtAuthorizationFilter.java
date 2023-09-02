package com.recipia.member.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 문자열 리터럴은 상수로 선언하여 사용하는 것이 좋다. 이렇게 하면 코드의 가독성이 향상되며, 나중에 변경이 필요할 때 한 곳에서만 수정하면 된다.
     */
    // fixme: 우리는 지금 cookie를 사용하지 않으니까 수정
    private static final String JWT_COOKIE_NAME = "jwt";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromCookiesWithoutException(request.getCookies());

        if (!StringUtils.hasText(token)) {
            // todo: 토큰이 없을때 에러 던지기
        }

    }

    // 예외를 발생시키지 않는 버전의 getTokenFromCookies 메소드
    private String getTokenFromCookiesWithoutException(Cookie[] cookies) {
        return Optional.ofNullable(cookies)
                .flatMap(cookieList -> Arrays.stream(cookieList).filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName())).findFirst())
                .map(Cookie::getValue)
                .orElse(null);

    }


}
