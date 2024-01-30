package com.recipia.member.adapter.in.web.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 문의사항 상세 내용을 담을 response Dto
 */
@NoArgsConstructor
@Data
public class AskViewResponseDto {

    private Long id; // 문의 ID
    private String title; // 문의 제목
    private String content; // 문의 내용
    private String answer; // 답변
    private String createDate;          // 문의 생성 날짜
    private String answerCreateDate;    // 답변 생성 날짜

    private AskViewResponseDto(Long id, String title, String content, String answer, String createDate, String answerCreateDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.createDate = createDate;
        this.answerCreateDate = answerCreateDate;
    }

    public static AskViewResponseDto of(Long id, String title, String content, String answer, String createDate, String answerCreateDate) {
        return new AskViewResponseDto(id, title, content, answer, createDate, answerCreateDate);
    }
}
