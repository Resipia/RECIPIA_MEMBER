package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.SignUpRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.domain.converter.MemberConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class SignUpController {

    private final SignUpUseCase signUpUseCase;

    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<Long>> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        Long memberId = signUpUseCase.signUp(MemberConverter.requestDtoToDomain(requestDto));
        return ResponseEntity.ok(
                ResponseDto.success(memberId)
        );
    }

}
