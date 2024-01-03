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

@RequiredArgsConstructor
@RequestMapping("/member/follow")
@RestController
public class FollowController {

    private final FollowUseCase followUseCase;
    private final FollowConverter followConverter;

    @PostMapping("/request")
    public ResponseEntity<ResponseDto<Long>> requestFollow (@Valid @RequestBody FollowRequestDto dto) {
        Long savedFollowId = followUseCase.followRequest(followConverter.requestDtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(savedFollowId)
        );
    }

}
