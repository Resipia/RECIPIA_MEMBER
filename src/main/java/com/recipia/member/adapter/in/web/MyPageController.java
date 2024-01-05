package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.MyPage;
import com.recipia.member.domain.converter.MyPageConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/member/myPage")
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageUseCase myPageUseCase;
    private final MyPageConverter myPageConverter;

    @PostMapping("/view")
    public ResponseEntity<ResponseDto<MyPageViewResponseDto>> view() {
        MyPage myPage = MyPage.of(SecurityUtils.getCurrentMemberId());
        myPage = myPageUseCase.viewMyPage(myPage);
        return ResponseEntity.ok(
                ResponseDto.success(myPageConverter.domainToResponseDto(myPage))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseDto<MyPageViewResponseDto>> update(@Valid @RequestBody UpdateMyPageRequestDto dto) {
        MyPage myPage = myPageUseCase.updateAndViewMyPage(myPageConverter.updateRequestDtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(myPageConverter.domainToResponseDto(myPage))
        );

    }

}
