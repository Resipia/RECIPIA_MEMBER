package com.recipia.member.oauth.service;

import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.adapter.out.persistence.constant.MemberStatus;
import com.recipia.member.adapter.out.persistenceAdapter.MemberAdapter;
import com.recipia.member.adapter.out.persistenceAdapter.SignUpAdapter;
import com.recipia.member.config.dto.TokenMemberInfoDto;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.converter.MemberConverter;
import com.recipia.member.oauth.controller.KakaoOAuthApi;
import com.recipia.member.oauth.response.KakaoAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

/**
 * OAuth2 전용 서비스 클래스
 * OAuth 2.0 기술을 사용한 사용자 인증을 위한 메소드다. OAuth2UserService -> @FunctionalInterface 라서 람다식으로 작성이 가능하다.
 * 여기가 매우 중요하다! 핵심 기능이다. 이 코드는 userDetailsService와 같은 역할을 하는 녀석이다.
 */
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberAdapter memberAdapter;
    private final PasswordEncoder passwordEncoder;
    private final SignUpAdapter signUpAdapter;
    private final MemberConverter memberConverter;
    private final KakaoOAuthApi kakaoOAuthApi;
    private final TokenUtils tokenUtils;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();
        KakaoAccount kakaoAccount = kakaoOAuthApi.getUserInfo(accessToken);

        Member member = processOAuthUser(kakaoAccount);
        TokenMemberInfoDto tokenMemberInfoDto = TokenMemberInfoDto.fromDomain(member);

        String jwtAccessToken = tokenUtils.generateAccessToken(tokenMemberInfoDto);
        Pair<String, LocalDateTime> refreshTokenPair = tokenUtils.generateRefreshToken(tokenMemberInfoDto);

        // 여기에서 OAuth2User를 반환하기 전에 JWT 토큰과 함께 다른 필요한 정보를 설정할 수 있습니다.
        // 예를 들어, JWT 토큰을 OAuth2User의 속성으로 추가할 수 있습니다.

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleType().toString())),
                Collections.singletonMap("accessToken", jwtAccessToken), // JWT 액세스 토큰 포함
                "name" // OAuth2User의 이름 속성 키
        );
    }

    private Member processOAuthUser(KakaoAccount kakaoAccount) {
        // 사용자 이메일을 바탕으로 기존 회원 확인
        String email = kakaoAccount.email();
        Optional<Member> existingMember = memberAdapter.findMemberByEmailAndStatus(email, MemberStatus.ACTIVE);

        if (existingMember.isPresent()) {
            // 기존 회원인 경우 정보 반환
            return existingMember.get();
        } else {
            // 신규 회원인 경우 회원가입 처리
            SignUpRequestDto signUpRequestDto = SignUpRequestDto.of(
                    email,
                    passwordEncoder.encode("임시비밀번호"),
                    kakaoAccount.name(),
                    kakaoAccount.nickname(),
                    null,
                    kakaoAccount.phoneNumber(),
                    null,
                    null,
                    null
            );

            Member member = memberConverter.requestDtoToDomain(signUpRequestDto);
            Long savedMemberId = signUpAdapter.signUpMember(member);
            member.setId(savedMemberId);

            return member;
        }
    }

}
