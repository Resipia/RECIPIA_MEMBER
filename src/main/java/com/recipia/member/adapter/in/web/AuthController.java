package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.PhoneNumberRequestDto;
import com.recipia.member.adapter.out.aws.TokyoSnsService;
import com.recipia.member.application.port.in.AuthUseCase;
import com.recipia.member.domain.Authentication;
import com.recipia.member.domain.converter.AuthConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthUseCase authUseCase;

    @PostMapping("/phone")
    public void sendPhoneNumber(@Valid PhoneNumberRequestDto requestDto) {
        authUseCase.verityPhoneNumber(AuthConverter.requestToDomain(requestDto));
    }

    @PostMapping("/phone")
    public void verifyCode(@RequestParam(required = true) String phoneNumbner , @RequestParam(required = true) String phoneNumbner ) {
        String phoneNumbner = "01012345678";
        Authentication authentication = Authentication.of(phoneNumbner, null);
        authUseCase.verityPhoneNumber(authentication);
    }

}
