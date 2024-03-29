package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.FollowRequestDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.FollowUseCase;
import com.recipia.member.domain.converter.FollowConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 팔로우 컨트롤러
 */
@RequiredArgsConstructor
@RequestMapping("/member/follow")
@RestController
public class FollowController {

    private final FollowUseCase followUseCase;
    private final FollowConverter followConverter;

    /**
     * 팔로우/언팔로우 요청
     */
    @PostMapping("/totalFollow")        // fixme: totalFollow 말고 request로 수정!! 테스트 코드도 수정!!
    public ResponseEntity<ResponseDto<Long>> requestFollowUnfollow (@Valid @RequestBody FollowRequestDto dto) {
        Long successId = followUseCase.followRequest(followConverter.requestDtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(successId)
        );
    }

}
