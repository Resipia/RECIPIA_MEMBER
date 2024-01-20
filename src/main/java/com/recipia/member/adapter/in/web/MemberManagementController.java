package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.EmailAvailableRequestDto;
import com.recipia.member.adapter.in.web.dto.request.FindEmailRequestDto;
import com.recipia.member.adapter.in.web.dto.request.ReportRequestDto;
import com.recipia.member.adapter.in.web.dto.request.TempPasswordRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.domain.converter.MemberConverter;
import com.recipia.member.domain.converter.ReportConverter;
import com.recipia.member.domain.converter.TempPasswordConverter;
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
    private final MemberConverter memberConverter;
    private final TempPasswordConverter tempPasswordConverter;

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

    /**
     * 이메일 찾기
     */
    @PostMapping("/find/email")
    public ResponseEntity<ResponseDto<String>> findEmail(@Valid @RequestBody FindEmailRequestDto dto) {
        String email = memberManagementUseCase.findEmail(memberConverter.findEmailDtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(email)
        );
    }


    /**
     * 임시 비밀번호 재발급
     */
    @PostMapping("/tempPassword")
    public ResponseEntity<ResponseDto<Void>> sendTempPassword(@Valid @RequestBody TempPasswordRequestDto dto) {
        memberManagementUseCase.sendTempPassword(tempPasswordConverter.dtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

}
