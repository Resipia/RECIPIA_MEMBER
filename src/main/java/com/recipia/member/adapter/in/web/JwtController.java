package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.JwtRepublishRequestDto;
import com.recipia.member.adapter.in.web.dto.response.JwtRepublishResponseDto;
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

/**
 * JWT 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/member/jwt")
@RestController
public class JwtController {

    private final JwtUseCase jwtUseCase;
    private final JwtConverter jwtConverter;

    /**
     * access token 재발행 요청
     */
    @PostMapping("/republish")
    public ResponseEntity<ResponseDto<JwtRepublishResponseDto>> republish (@Valid @RequestBody JwtRepublishRequestDto republishReqDto) {
        String accessToken = jwtUseCase.republishAccessToken(jwtConverter.requestDtoToDomain(republishReqDto));
        return ResponseEntity.ok(
                ResponseDto.success(JwtRepublishResponseDto.of(accessToken))
        );
    }

}
