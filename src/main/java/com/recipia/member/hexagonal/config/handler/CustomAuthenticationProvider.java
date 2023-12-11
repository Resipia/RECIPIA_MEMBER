package com.recipia.member.hexagonal.config.handler;

import com.recipia.member.hexagonal.config.dto.SecurityUserDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 사용자 로그인 정보 추출
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        String userCryptPassword = (String) token.getCredentials();

        // DB에서 사용자 정보 조회
        SecurityUserDetailsDto securityUserDetailsDto = (SecurityUserDetailsDto) userDetailsService.loadUserByUsername(username);

        // 암호화된 비밀번호 비교
        if (!bCryptPasswordEncoder.matches(userCryptPassword, securityUserDetailsDto.getMemberDto().password())) {
            throw new BadCredentialsException(securityUserDetailsDto.getUsername() + "의 비밀번호가 잘못되었습니다.");
        }

        // 인증 성공 시 반환 객체 생성 및 반환
        return new UsernamePasswordAuthenticationToken(securityUserDetailsDto, userCryptPassword, securityUserDetailsDto.getAuthorities());
    }

    /**
     * 현재 제공자가 지원하는 인증 타입인지 확인.
     *
     * @param authentication 인증 타입 클래스
     * @return 지원하는 경우 true, 그렇지 않으면 false.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
