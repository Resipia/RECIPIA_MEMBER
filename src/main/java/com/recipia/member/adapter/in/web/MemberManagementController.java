package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.EmailAvailableRequestDto;
import com.recipia.member.adapter.in.web.dto.request.ReportRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.domain.converter.ReportConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원 관리 컨트롤러
 */
@RequestMapping("/member/management")
@RequiredArgsConstructor
@RestController
public class MemberManagementController {

    private final MemberManagementUseCase memberManagementUseCase;
    private final ReportConverter reportConverter;

    /**
     * 이메일 중복체크 요청
     */
    @PostMapping("/checkDupEmail")
    public ResponseEntity<ResponseDto<Boolean>> checkDuplicateEmail(@Valid @RequestBody EmailAvailableRequestDto requestDto) {
        boolean isEmailAvailable = memberManagementUseCase.isEmailAvailable(requestDto.getEmail());
        return ResponseEntity.ok(
                ResponseDto.success(isEmailAvailable)
        );
    }

    /**
     * 회원 신고
     */
    @PostMapping("/report")
    public ResponseEntity<ResponseDto<Void>> reportMember(@Valid @RequestBody ReportRequestDto dto) {
        Long savedReportId = memberManagementUseCase.reportMember(reportConverter.dtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

}
