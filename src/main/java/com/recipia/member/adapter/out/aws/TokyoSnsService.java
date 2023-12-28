package com.recipia.member.adapter.out.aws;

import com.recipia.member.config.aws.SnsConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokyoSnsService {

    private final SnsConfig snsConfig;
    private static final String TOKYO_REGION = "ap-northeast-1";


    /**
     * 서울 리전에서 사용하는 SnsClient의 빈과 겹치지 않게 도쿄 리전은 따로 객체를 생성 (빈 설정 X)
     */
    public SnsClient tokyoSnsClient() {
        return SnsClient.builder()
                .region(Region.of(TOKYO_REGION))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(snsConfig.getAwsAccessKey(), snsConfig.getAwsSecretKey())))
                .build();
    }

    // 인증번호를 포함한 SMS 메시지 보내기
    public void sendVerificationCode(String phoneNumber) {
        String verificationCode = generateRandomCode();
        String message = String.format("[Recipia] 인증번호[%s]를 입력해주세요.", verificationCode);

        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        try {
            PublishResponse response = tokyoSnsClient().publish(request);
            log.info("Sent message {} to {} with messageId {}", message, phoneNumber, response.messageId());
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
        }
    }

    // 랜덤 6자리 숫자 생성
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000부터 999999까지
        return String.valueOf(number);
    }

}
