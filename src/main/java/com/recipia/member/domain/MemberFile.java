package com.recipia.member.domain;

import com.recipia.member.domain.auditingfield.UpdateDateTime;
import com.recipia.member.hexagonal.adapter.out.persistence.member.MemberEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** 회원 파일 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class MemberFile extends UpdateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_file_id", nullable = false)
    private Long id;                // 회원 파일 pk

    @ToString.Exclude
    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private MemberEntity member;              // 회원 pk

    @Column(name = "flpth", nullable = false)
    private String filePath;        // 파일 경로

    @Column(name = "origin_file_nm", nullable = false)
    private String originFileName;  // 원본 파일명

    @Column(name = "strd_file_nm", nullable = false)
    private String storedFileName;  // 저장 파일명

    @Column(name = "file_extsn", nullable = false)
    private String fileExtension;   // 확장자

    @Column(name = "file_size", nullable = false)
    private Integer fileSize;       // 파일 크기

    @Column(name = "del_yn", nullable = false)
    private String delYn;           // 삭제여부

    private MemberFile(MemberEntity member, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn) {
        this.member = member;
        this.filePath = filePath;
        this.originFileName = originFileName;
        this.storedFileName = storedFileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.delYn = delYn;
    }

    // 생성자 factory method of 선언
    public static MemberFile of(MemberEntity member, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn) {
        return new MemberFile(member, filePath, originFileName, storedFileName, fileExtension, fileSize, delYn);
    }

}
