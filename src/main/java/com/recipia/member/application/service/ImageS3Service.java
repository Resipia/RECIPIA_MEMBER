package com.recipia.member.application.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.recipia.member.application.port.out.port.MemberPort;
import com.recipia.member.common.exception.ErrorCode;
import com.recipia.member.common.exception.MemberApplicationException;
import com.recipia.member.domain.Member;
import com.recipia.member.domain.MemberFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import net.coobird.thumbnailator.Thumbnails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * S3 이미지 저장 서비스
 * 도움받은 블로그(코드) 출처: https://growth-coder.tistory.com/116
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ImageS3Service {

    private final AmazonS3 amazonS3;
    private final MemberPort memberPort;

    @Value("${spring.cloud.aws.s3.bucketName}")
    private String bucketName; //버킷 이름


    /**
     * 데이터베이스에 저장할 MemberFile 객체를 생성하여 반환한다.
     */
    public MemberFile createMemberFile(MultipartFile image, Long savedMemberId) {

        // 1. input 파라미터 검증
        validateInput(image, savedMemberId);
        String originFileName = image.getOriginalFilename(); //원본 이미지 이름

        // 2. 파일 확장자 추출 및 검증
        String fileExtension = getFileExtension(originFileName);
        validateFileExtension(fileExtension);

        // 3. 파일 도메인을 만들기 위한 값 세팅
        String storedFileNameWithExtension = changeFileName(fileExtension); // 새로 생성된 이미지 이름 (UUID 적용)
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜를 [년/월/일] 형식으로 가져와서 최종 저장될 파일 경로를 만들어 준다.
        String finalPath = "member/" + datePath + "/" + savedMemberId + "/" + storedFileNameWithExtension; // s3 파일 저장 경로
        String objectUrl = uploadImageToS3(image, fileExtension, finalPath); // 저장된 s3 객체의 URL
        Integer fileSize = (int) image.getSize(); // 파일 사이즈 추출

        // 4. 파일 도메인으로 변환
        return MemberFile.of(
                Member.of(savedMemberId),
                finalPath,                      // s3 파일 저장 경로
                objectUrl,                      // s3 객체 url
                originFileName,                 // 파일 원본 이름
                storedFileNameWithExtension,    // s3에 저장된 파일 이름
                fileExtension,                  // 확장자
                fileSize,                        // 사이즈
                "N"

        );
    }

    /**
     * 이미지를 S3에 업로드하고 저장된 s3 객체(이미지) url을 반환한다.
     * [MultipartFile] image 객체와 확장자, 이미지가 저장될 s3의 경로를 매게변수로 받는다.
     * 이미지 압축 및 크기 조정 로직을 수정하여 파일 사이즈에 따라 동적으로 조절한다.
     */
    public String uploadImageToS3(MultipartFile image, String extension, String finalPath) {

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // 원본 이미지 사이즈 확인
            long originalSize = image.getSize();

            // 원본 이미지 스트림
            InputStream originalInputStream = image.getInputStream();

            // 원본 사이즈가 10MB를 초과하는 경우 사이즈 조정 로직 실행
            if (originalSize > 10485760) { // 10MB in bytes
                // 이미지 사이즈 조정
                Thumbnails.of(originalInputStream)
                        .size(720, 1280) // 기본 해상도 설정
                        .outputQuality(0.75) // 품질 조정
                        .toOutputStream(outputStream);

                // 조정된 이미지 바이트 배열
                byte[] compressedImageBytes = outputStream.toByteArray();
                // 조정된 이미지 스트림
                ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedImageBytes);

                // 메타데이터 설정
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/" + extension.substring(1));
                metadata.setContentLength(compressedImageBytes.length); // 압축된 이미지 길이 설정

                // S3에 업로드
                amazonS3.putObject(new PutObjectRequest(bucketName, finalPath, inputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 스트림 닫기
                inputStream.close();
            } else {
                // 원본 사이즈가 10MB 이하인 경우, 원본 이미지를 직접 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType("image/" + extension.substring(1));
                metadata.setContentLength(originalSize); // 원본 이미지 길이 설정

                amazonS3.putObject(new PutObjectRequest(bucketName, finalPath, originalInputStream, metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }

            // 저장된 객체의 URL 반환
            return amazonS3.getUrl(bucketName, finalPath).toString();
        } catch (IOException e) {
            throw new MemberApplicationException(ErrorCode.S3_UPLOAD_ERROR);
        }
    }

    /**
     * s3에 이미지를 저장할때 UUID와 함께 저장되도록 만들어준다.
     * 중복된 이름을 제거하기 위함
     */
    public String changeFileName(String fileExtension) {
        String random = UUID.randomUUID().toString();
        return random + fileExtension;
    }

    /**
     * 이미지(파일)를 삭제한다.
     */
    public void deleteImage(String key) {
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketName, key);
        amazonS3.deleteObject(deleteRequest);
    }

    /**
     * Pre-Signed URL을 생성하고 반환한다.
     * @param filePath 버킷에서의 파일 경로
     * @param duration URL의 유효 시간(분 단위)
     * @return 생성된 Pre-Signed URL
     */
    public String generatePreSignedUrl(String filePath, int duration) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * duration; // 예: 60분
        expiration.setTime(expTimeMillis);

        // Pre-Signed URL 요청 생성
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, filePath)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        // Pre-Signed URL 생성
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }


    /**
     * 파일의 확장자를 안전하게 추출한다.
     */
    private static String getFileExtension(String originFileName) {
        String fileExtension = "";
        if (originFileName != null && originFileName.contains(".")) {
            fileExtension = originFileName.substring(originFileName.lastIndexOf("."));
        }
        return fileExtension;
    }

    /**
     * 파라미터로 들어온 값들의 유효성을 검증한다.
     */
    private static void validateInput(MultipartFile image, Long savedMemberId) {
        if (image == null || image.isEmpty()) {
            throw new MemberApplicationException(ErrorCode.S3_UPLOAD_FILE_NOT_FOUND);
        }
        if (savedMemberId == null) {
            throw new MemberApplicationException(ErrorCode.USER_NOT_FOUND);
        }
    }

    /**
     * 파일의 확장자가 지원되는 확장자인지 검증한다.
     */
    private static void validateFileExtension(String fileExtension) {
        // 지원되는 이미지 확장자 목록
        List<String> supportedExtensions = Arrays.asList(".jpg", ".jpeg", ".png");

        // 파일 확장자 검사
        if (!supportedExtensions.contains(fileExtension.toLowerCase())) {
            throw new MemberApplicationException(ErrorCode.INVALID_FILE_TYPE);
        }
    }

}
