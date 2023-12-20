package com.recipia.member.hexagonal.adapter.out.persistence;

import com.recipia.member.hexagonal.adapter.out.persistence.auditingfield.UpdateDateTimeForEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Objects;

/** 회원 파일 */
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "member_file")
public class MemberFileEntity extends UpdateDateTimeForEntity {

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

    private MemberFileEntity(Long id, MemberEntity member, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn) {
        this.id = id;
        this.member = member;
        this.filePath = filePath;
        this.originFileName = originFileName;
        this.storedFileName = storedFileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.delYn = delYn;
    }

    // 새 엔티티 생성용 팩토리 메소드
    public static MemberFileEntity of(MemberEntity member, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn) {
        return new MemberFileEntity(null, member, filePath, originFileName, storedFileName, fileExtension, fileSize, delYn);
    }

    // 기존 엔티티 로드용 팩토리 메소드
    public static MemberFileEntity of(Long id, MemberEntity member, String filePath, String originFileName, String storedFileName, String fileExtension, Integer fileSize, String delYn) {
        return new MemberFileEntity(id, member, filePath, originFileName, storedFileName, fileExtension, fileSize, delYn);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberFileEntity that)) return false;
        return this.id != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
