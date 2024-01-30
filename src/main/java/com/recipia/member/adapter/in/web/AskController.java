package com.recipia.member.adapter.in.web;

import com.recipia.member.adapter.in.web.dto.request.AskRequestDto;
import com.recipia.member.adapter.in.web.dto.response.AskListResponseDto;
import com.recipia.member.adapter.in.web.dto.response.PagingResponseDto;
import com.recipia.member.adapter.in.web.dto.response.ResponseDto;
import com.recipia.member.application.port.in.AskUseCase;
import com.recipia.member.domain.converter.AskConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 문의사항 관련 컨트롤러
 */
@RequestMapping("/member/ask")
@RequiredArgsConstructor
@RestController
public class AskController {

    private final AskUseCase askUseCase;
    private final AskConverter askConverter;

    /**
     * 문의사항 등록
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto<Long>> createAsk(@Valid @RequestBody AskRequestDto dto) {
        Long createdId = askUseCase.createAsk(askConverter.dtoToDomain(dto));
        return ResponseEntity.ok(
                ResponseDto.success(createdId)
        );
    }

    /**
     * 문의사항 목록 가져오기
     */
    @GetMapping("/list")
    public ResponseEntity<PagingResponseDto<AskListResponseDto>> getList(int page, int size) {
        PagingResponseDto<AskListResponseDto> result = askUseCase.getAskList(page, size);
        return ResponseEntity.ok(result);
    }

}
