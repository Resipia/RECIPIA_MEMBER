package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.*;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MemberManagementUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.converter.ChangePasswordConverter;
import com.recipia.member.domain.converter.MemberConverter;
import com.recipia.member.domain.converter.ReportConverter;
import com.recipia.member.domain.converter.TempPasswordConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final SecurityUtils securityUtils;
    private final ChangePasswordConverter changePasswordConverter;

    /**
     * 전화번호 중복체크 요청
     */
    @PostMapping("/checkDupTelNo")
    public ResponseEntity<ResponseDto<Boolean>> checkDupTelNo(@Valid @RequestBody TelNoAvailableRequestDto dto) {
        boolean isTelNoAvailable = memberManagementUseCase.isTelNoAvailable(dto.getTelNo());
        return ResponseEntity.ok(
                ResponseDto.success(isTelNoAvailable)
        );
    }

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


    /**
     * 닉네임 중복체크
     */
    @PostMapping("/checkDupNickname")
    public ResponseEntity<ResponseDto<Boolean>> checkDupNickname(@Valid @RequestBody NicknameAvailableRequestDto dto) {
        boolean isNicknameAvailable = memberManagementUseCase.isNicknameAvailable(dto.getNickname());
        return ResponseEntity.ok(
                ResponseDto.success(isNicknameAvailable)
        );
    }

    /**
     * 닉네임 상세에 보여줄 회원 프로필 사진 요청
     */
    @PostMapping("/getProfile")
    public ResponseEntity<ResponseDto<String>> getProfileImage(@Valid @RequestBody MemberProfileRequestDto dto) {
        String profilePreUrl = memberManagementUseCase.getProfilePreUrl(dto.getMemberId());
        return ResponseEntity.ok(
                ResponseDto.success(profilePreUrl)
        );
    }

    /**
     * 비밀번호 수정
     */
    @PostMapping("/updatePassword")
    public ResponseEntity<ResponseDto<Long>> updatePassword(@Valid @RequestBody ChangePasswordRequestDto dto) {
        Long updatedCount = memberManagementUseCase.changePassword(changePasswordConverter.dtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(updatedCount)
        );
    }
}
