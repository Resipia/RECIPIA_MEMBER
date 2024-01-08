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

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthUseCase authUseCase;
    private final AuthConverter authConverter;

    @PostMapping("/phone")
    public ResponseEntity<ResponseDto<Void>> sendPhoneNumber(@Valid @RequestBody PhoneNumberRequestDto requestDto) {
        authUseCase.verifyPhoneNumber(authConverter.phoneNumberRequestDtoToDomain(requestDto));
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

    @PostMapping("/check/verifyCode")
    public ResponseEntity<ResponseDto<Boolean>> checkVerifyCode(@Valid @RequestBody CheckVerifyCodeRequestDto requestDto) {
        boolean isValidCode = authUseCase.checkVerifyCode(authConverter.checkVerifyCodeRequestDtoToDomain(requestDto));
        return ResponseEntity.ok(
                ResponseDto.success(isValidCode)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(HttpServletRequest request) {
        String authorizationHeaderValue = request.getHeader("Authorization");
        String accessToken = TokenUtils.extractAccessToken(authorizationHeaderValue);

        Logout logout = Logout.of(SecurityUtils.getCurrentMemberId(), accessToken);
        authUseCase.logout(logout);

        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }


}
