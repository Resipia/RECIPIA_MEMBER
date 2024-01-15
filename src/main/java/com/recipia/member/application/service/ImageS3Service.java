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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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

        // file order는 1부터 시작
        Integer fileOrder = memberPort.findMaxFileOrder(savedMemberId) + 1;

        // 3. 파일 도메인을 만들기 위한 값 세팅
        String storedFileNameWithExtension = changeFileName(fileExtension); // 새로 생성된 이미지 이름 (UUID 적용)
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")); // 현재 날짜를 [년/월/일] 형식으로 가져와서 최종 저장될 파일 경로를 만들어 준다.
        String finalPath = "member/" + datePath + "/" + savedMemberId + "/" + storedFileNameWithExtension; // s3 파일 저장 경로
        String objectUrl = uploadImageToS3(image, fileExtension, finalPath); // 저장된 s3 객체의 URL
        Integer fileSize = (int) image.getSize(); // 파일 사이즈 추출

        // 4. 파일 도메인으로 변환
        return MemberFile.of(
                Member.of(savedMemberId),
                fileOrder,                      // 파일 정렬 순서
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
     */
    public String uploadImageToS3(MultipartFile image, String extension, String finalPath) {

        ObjectMetadata metadata = new ObjectMetadata();
        // todo 만약 파일이면 image/ 만으로는 처리 불가 (추후 파일용 메타 데이터도 추가해 주기)
        metadata.setContentType("image/" + extension.substring(1));

        // 이미지를 s3에 업로드(PutObject) 한다.
        try {
            amazonS3.putObject(new PutObjectRequest(
                    bucketName, finalPath, image.getInputStream(), metadata
            ).withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new MemberApplicationException(ErrorCode.S3_UPLOAD_ERROR);
        }

        // 저장된 객체의 url 반환
        return amazonS3.getUrl(bucketName, finalPath).toString();
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
        java.util.Date expiration = new java.util.Date();
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
