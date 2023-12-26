package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.JwtRepublishRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.JwtUseCase;
import com.recipia.member.domain.converter.JwtConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member/jwt")
@RestController
public class JwtController {

    private final JwtUseCase jwtUseCase;

    @PostMapping("/republish")
    public ResponseEntity<ResponseDto<Void>> republish (@Valid @RequestBody JwtRepublishRequestDto republishReqDto) {
        jwtUseCase.republishAccessToken(JwtConverter.requestToDomain(republishReqDto));
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

}
