package com.recipia.member.application.service;

import com.amazonaws.services.s3.AmazonS3;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.config.TestMockS3Config;
import com.recipia.member.config.TotalTestSupport;
import com.recipia.member.domain.MemberFile;
import io.findify.s3mock.S3Mock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 이 테스트에서는 실제 S3 서비스를 사용하지 않으므로
 * 네트워크 연결이나 S3의 실제 상태와는 독립적이다.
 */
@Import(TestMockS3Config.class)
@DisplayName("[통합] S3에 이미지를 저장하는 서비스 테스트")
class ImageS3ServiceTest extends TotalTestSupport {


    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private ImageS3Service sut; // System Under Test

    private static final String BUCKET_NAME = "recipia-file";

    @BeforeAll
    static void setUp(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        s3Mock.start();
        amazonS3.createBucket(BUCKET_NAME);
    }

    @AfterAll
    static void tearDown(@Autowired S3Mock s3Mock, @Autowired AmazonS3 amazonS3) {
        amazonS3.shutdown();
        s3Mock.stop();
    }

    @Test
    @DisplayName("[happy] S3에 이미지 저장을 시도하면 업로드가 완료되고 도메인 객체인 MemberFile을 반환한다.")
    void testUploadImageToS3() throws IOException {
        //given
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image content".getBytes());

        Long memberId = 123L;

        //when
        MemberFile memberFile = sut.createMemberFile(mockImage, memberId);

        //then
        assertNotNull(memberFile);
        assertEquals("test.jpg", memberFile.getOriginFileNm());
        assertNotNull(memberFile.getStoredFileNm());
        assertNotNull(memberFile.getObjectUrl());
        Integer mockImageSize = Integer.parseInt(String.valueOf(mockImage.getSize()));
        Assertions.assertThat(mockImageSize).isEqualTo(memberFile.getFileSize());
    }

    @Test
    @DisplayName("[bad] 지원되지 않는 파일 형식을 업로드 시도하면 예외가 발생한다.")
    void createMemberFileEntityException1() {
        //given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "This is a text file.".getBytes());

        //when & then
        Assertions.assertThatThrownBy(() -> sut.createMemberFile(mockFile, 1L))
                .isInstanceOf(MemberApplicationException.class)
                .hasMessageContaining("S3에 업로드 할 수 없는 파일 타입입니다.")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_FILE_TYPE);
    }

    @Test
    @DisplayName("[bad] memberId가 존재하지 않을 경우 예외가 발생한다.")
    void createMemberFileEntityException2() {
        //given
        MockMultipartFile mockImage = new MockMultipartFile(
                "image", "empty.jpg", "image/jpeg", new byte[100]);

        //when & then
        Assertions.assertThatThrownBy(() -> sut.createMemberFile(mockImage, null))
                .isInstanceOf(MemberApplicationException.class)
                .hasMessageContaining("유저를 찾을 수 없습니다.")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @DisplayName("[bad] 저장할 파일이 없는 경우 예외가 발생한다.")
    void createMemberFileEntityException3() {
        Long memberId = 123L;

        //when & then
        Assertions.assertThatThrownBy(() -> sut.createMemberFile(null, memberId))
                .isInstanceOf(MemberApplicationException.class)
                .hasMessageContaining("업로드할 파일이 존재하지 않습니다.")
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.S3_UPLOAD_FILE_NOT_FOUND);
    }

}