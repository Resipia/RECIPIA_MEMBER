package com.recipia.member.hexagonal.adapter.in.web;

import com.recipia.member.hexagonal.adapter.in.web.dto.LoginRequest;
import com.recipia.member.hexagonal.application.port.in.MemberUseCase;
import com.recipia.member.hexagonal.domain.SignIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final MemberUseCase memberUseCase;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 로그인 로직을 usecase를 통해 처리
        SignIn result = memberUseCase.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(result);
    }

}
