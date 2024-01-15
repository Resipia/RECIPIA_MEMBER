package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.SignUpRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.SignUpUseCase;
import com.recipia.member.domain.converter.MemberConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 회원 가입 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/member")
@RestController
public class SignUpController {

    private final SignUpUseCase signUpUseCase;
    private final MemberConverter memberConverter;

    /**
     * 회원 가입 요청
     */
    @PostMapping("/signUp")
    public ResponseEntity<ResponseDto<Long>> signUp(@Valid @ModelAttribute SignUpRequestDto requestDto) {
        MultipartFile profileImage = requestDto.getProfileImage();
        Long memberId = signUpUseCase.signUp(memberConverter.requestDtoToDomain(requestDto), profileImage);
        return ResponseEntity.ok(
                ResponseDto.success(memberId)
        );
    }

}
