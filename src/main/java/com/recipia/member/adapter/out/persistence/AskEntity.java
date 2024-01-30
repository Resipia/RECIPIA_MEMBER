package com.recipia.member.adapter.out.persistence;

import com.recipia.member.adapter.out.persistence.auditingfield.UpdateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 문의사항 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "ask")
public class AskEntity extends UpdateDateTimeForEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ask_id")
    private Long id; // 문의 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "title", length = 50, nullable = false)
    private String title; // 문의 제목

    @Column(name = "content", length = 100, nullable = false)
    private String content; // 문의 내용

    @Column(name = "answer", length = 100)
    private String answer; // 답변

    private AskEntity(Long id, MemberEntity memberEntity, String title, String content, String answer) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.title = title;
        this.content = content;
        this.answer = answer;
    }

    public static AskEntity of(Long askId, MemberEntity memberEntity, String title, String content, String answer) {
        return new AskEntity(askId, memberEntity, title, content, answer);
    }

    public static AskEntity of(MemberEntity memberEntity, String title, String content, String answer) {
        return new AskEntity(null, memberEntity, title, content, answer);
    }

    public static AskEntity of(MemberEntity memberEntity, String title, String content) {
        return new AskEntity(null, memberEntity, title, content, null);
    }
}
