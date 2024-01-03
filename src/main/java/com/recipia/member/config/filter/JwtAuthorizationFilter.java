package com.recipia.member.config.filter;

import com.recipia.member.config.dto.TokenMemberInfoDto;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.config.jwt.TokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenValidator tokenValidator;

    /**
     * 문자열 리터럴은 상수로 선언하여 사용하는 것이 좋다. 이렇게 하면 코드의 가독성이 향상되며, 나중에 변경이 필요할 때 한 곳에서만 수정하면 된다.
     */
    private static final String ACCESS_TOKEN_TYPE = "access";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // Remove "Bearer " prefix

            if (validateToken(token)) {
                UsernamePasswordAuthenticationToken authenticationToken = extractUserDetails(token);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    // 토큰의 유효성을 검증하는 메서드
    private boolean validateToken(String token) {
        log.info("start");
        return tokenValidator.isValidToken(token, ACCESS_TOKEN_TYPE);
    }

    // 토큰에서 사용자 정보를 추출하여 UsernamePasswordAuthenticationToken 객체로 반환하는 메서드
    private UsernamePasswordAuthenticationToken extractUserDetails(String token) {
        // JWT 토큰에서 사용자 정보를 추출
        Long memberId = TokenUtils.getMemberIdFromToken(token);
        String email = TokenUtils.getEmailFromToken(token);
        String nickname = TokenUtils.getNicknameFromToken(token);
        String role = TokenUtils.getRoleFromToken(token);

        // 사용자의 권한을 토큰에서 직접 추출
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        // TokenMemberInfoDto 객체 생성
        TokenMemberInfoDto tokenMemberInfoDto = TokenMemberInfoDto.of(memberId, email, nickname);

        // TokenMemberInfoDto를 principal로 사용하여 UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(tokenMemberInfoDto, null, authorities);
    }

}
