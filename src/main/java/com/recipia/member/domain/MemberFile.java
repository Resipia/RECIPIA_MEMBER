package com.recipia.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원 프로필 이미지와 연관된 파일 도메인 객체
 */
@NoArgsConstructor
@Getter
@Setter
public class MemberFile {


    private Long id;
    private Member member; // 어떤 회원과 연관있는지
    private String storedFilePath; // 저장된 파일 경로 url
    private String objectUrl; // 저장된 객체 url (사진 바로 볼수있음)
    private String originFileNm;    // 원본 파일 이름
    private String storedFileNm;  // 저장된 파일 이름
    private String fileExtension; // 파일 확장자 (jpg, jpeg, png)
    private Integer fileSize; // 파일 크기
    private String delYn; // 삭제 여부


    @Builder
    public MemberFile(Long id, Member member, String storedFilePath, String objectUrl, String originFileNm, String storedFileNm, String fileExtension, Integer fileSize, String delYn) {
        this.id = id;
        this.member = member;
        this.storedFilePath = storedFilePath;
        this.objectUrl = objectUrl;
        this.originFileNm = originFileNm;
        this.storedFileNm = storedFileNm;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.delYn = delYn;
    }

    public static MemberFile of(Long id, Member member, String storedFilePath, String objectUrl, String originFileNm, String storedFileNm, String fileExtension, Integer fileSize, String delYn) {
        return new MemberFile(id, member, storedFilePath, objectUrl, originFileNm, storedFileNm, fileExtension, fileSize, delYn);
    }

    /**
     * ImageS3Service 클래스에서 사용
     * MultipartFile로 이미지 저장에 필요한 값을 꺼내서 도메인으로 변환
     */
    public static MemberFile of(Member member,  String storedFilePath, String objectUrl, String originFileNm, String storedFileNm, String fileExtension, Integer fileSize, String delYn) {
        return new MemberFile(null, member, storedFilePath, objectUrl, originFileNm, storedFileNm, fileExtension, fileSize, delYn);
    }


}
