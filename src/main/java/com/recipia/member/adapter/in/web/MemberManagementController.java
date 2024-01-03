package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.EmailAvailableRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/management")
@RequiredArgsConstructor
@RestController
public class MemberManagementController {

    private final MemberManagementUseCase memberManagementUseCase;

    /**
     * 이메일 중복체크
     */
    @PostMapping("/checkDupEmail")
    public ResponseEntity<ResponseDto<Boolean>> checkDuplicateEmail(@Valid @RequestBody EmailAvailableRequestDto requestDto) {
        boolean isEmailAvailable = memberManagementUseCase.isEmailAvailable(requestDto.getEmail());
        return ResponseEntity.ok(
                ResponseDto.success(isEmailAvailable)
        );
    }


}
