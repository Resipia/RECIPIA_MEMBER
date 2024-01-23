package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.UpdateMyPageRequestDto;
import com.recipia.member.adapter.in.web.dto.response.FollowListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.MyPageViewResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.MyPageUseCase;
import com.recipia.member.common.utils.SecurityUtils;
import com.recipia.member.domain.MyPage;
import com.recipia.member.domain.converter.MyPageConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 마이페이지 컨트롤러
 */
@RequestMapping("/member/myPage")
@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageUseCase myPageUseCase;
    private final MyPageConverter myPageConverter;
    private final SecurityUtils securityUtils;

    /**
     * 다른 유저가 마이페이지 조회
     */
    @PostMapping("/view")
    public ResponseEntity<ResponseDto<MyPageViewResponseDto>> view() {
        MyPage myPage = myPageUseCase.viewMyPage(securityUtils.getCurrentMemberId());
        return ResponseEntity.ok(
                ResponseDto.success(myPageConverter.domainToResponseDto(myPage))
        );
    }

    /**
     * 마이페이지 수정
     */
    @PostMapping("/update")
    public ResponseEntity<ResponseDto<Void>> update(@Valid @RequestBody UpdateMyPageRequestDto dto) {
        MultipartFile profileImage = dto.getProfileImage();
        myPageUseCase.updateMyPage(myPageConverter.updateRequestDtoToDomain(dto), profileImage);
        return ResponseEntity.ok(
                ResponseDto.success()
        );
    }

    /**
     * [READ] type이 follow일땐 팔로잉 목록 조회, type이 follower일땐 팔로워 목록 조회
     */
    @GetMapping("/followList")
    public ResponseEntity<PagingResponseDto<FollowListResponseDto>> getFollowingList(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "targetMemberId") Long targetMemberId,
            @RequestParam(value = "type") String type                       // 팔로잉 목록인지, 팔로워 목록인지 구별
    ) {
        PagingResponseDto<FollowListResponseDto> result = myPageUseCase.getFollowList(targetMemberId, type, page, size);
        return ResponseEntity.ok(result);
    }
}
