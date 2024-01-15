package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.CheckVerifyCodeRequestDto;
import com.recipia.member.adapter.in.web.dto.request.PhoneNumberRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.config.jwt.TokenUtils;
import com.recipia.member.domain.Logout;
import com.recipia.member.domain.converter.AuthConverter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 본인 인증 관련 컨트롤러
 */
@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthUseCase authUseCase;
    private final AuthConverter authConverter;
    private final SecurityUtils securityUtils;

    /**
     * 휴대폰 번호로 인증코드 전송
     */
    @PostMapping("/phone")
    public ResponseEntity<ResponseDto<Void>> sendPhoneNumber(@Valid @RequestBody PhoneNumberRequestDto requestDto) {
        authUseCase.verifyPhoneNumber(authConverter.phoneNumberRequestDtoToDomain(requestDto));
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

    /**
     * 인증코드 검증
     */
    @PostMapping("/check/verifyCode")
    public ResponseEntity<ResponseDto<Boolean>> checkVerifyCode(@Valid @RequestBody CheckVerifyCodeRequestDto requestDto) {
        boolean isValidCode = authUseCase.checkVerifyCode(authConverter.checkVerifyCodeRequestDtoToDomain(requestDto));
        return ResponseEntity.ok(
                ResponseDto.success(isValidCode)
        );
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest request) {
        Logout logout = extractLogoutDetailsFromRequest(request);
        authUseCase.logout(logout);

        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

    /**
     * 회원 탈퇴
     */
    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDto<Void>> withdraw(HttpServletRequest request) {
        Logout logout = extractLogoutDetailsFromRequest(request);
        authUseCase.logout(logout);
        authUseCase.deactivateMember(logout.getMemberId());
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

    /**
     * header에서 access token 추출하고 Logout 도메인 반환
     */
    private Logout extractLogoutDetailsFromRequest(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader("Authorization");
        String accessToken = TokenUtils.extractAccessToken(authorizationHeaderValue);

        return Logout.of(securityUtils.getCurrentMemberId(), accessToken);
    }

}
