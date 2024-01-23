package com.recipia.member.adapter.in.web.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 페이징 응답 전용 dto 객체
 * 페이징이 아닐때는 ResponseDto를 사용하면 된다.
 */
@NoArgsConstructor
@Data
public class PagingResponseDto<T> {

    List<T> content;
    Long totalCount;

    @Builder
    private PagingResponseDto(List<T> content, Long totalCount) {
        this.content = content;
        this.totalCount = totalCount;
    }

    public static <T> PagingResponseDto<T> of(List<T> content, Long totalCount){
        return new PagingResponseDto<>(content, totalCount);
    }

}
