package com.recipia.member.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 문의사항 도메인 객체
 */
@Getter
@NoArgsConstructor
public class Ask {

    private Long id; // 문의 ID
    private Long memberId;
    private String title; // 문의 제목
    private String content; // 문의 내용
    private String answer; // 답변
    private String createDate;          // 문의 생성 날짜
    private String answerCreateDate;    // 답변 생성 날짜

    public Ask(Long id, Long memberId, String title, String content, String answer, String createDate, String answerCreateDate) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.answer = answer;
        this.createDate = createDate;
        this.answerCreateDate = answerCreateDate;
    }

    public static Ask of(Long id, Long memberId, String title, String content, String answer, String createDate, String answerCreateDate) {
        return new Ask(id, memberId, title, content, answer, createDate, answerCreateDate);
    }

    public static Ask of(Long memberId, String title, String content, String answer, String createDate, String answerCreateDate) {
        return new Ask(null, memberId, title, content, answer, createDate, answerCreateDate);
    }

    public static Ask of(Long memberId, String title, String content) {
        return new Ask(null, memberId, title, content, null, null, null);
    }

    public static Ask of(Long id, Long memberId) {
        return new Ask(id, memberId, null, null, null, null, null);
    }

}
