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

import java.util.Optional;

/**
 * 도쿄 리전 SNS 발행 서비스
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class TokyoSnsService {

    private final SnsConfig snsConfig;
    private static final String TOKYO_REGION = "ap-northeast-1";
    private static Optional<SnsClient> tokyoSnsClient = Optional.empty(); // Optional 사용

    /**
     * 서울 리전과 동일한 객체 타입인 SnsClient를 사용하기때문에 bean 충돌이 나지 않도록
     * TokyoSnsService 클래스에서 따로 생성해서 사용하는 SnsClient 객체
     */
    private SnsClient tokyoSnsClient() {
        if (tokyoSnsClient.isEmpty()) { // Optional의 isEmpty() 메소드를 사용
            tokyoSnsClient = Optional.of(SnsClient.builder()
                    .region(Region.of(TOKYO_REGION))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(snsConfig.getAwsAccessKey(), snsConfig.getAwsSecretKey())))
                    .build());
        }
        return tokyoSnsClient.get(); // Optional에서 값을 가져옴
    }

    // 인증번호를 포함한 SMS 메시지 보내기
    public void sendVerificationCode(String phoneNumber, String verificationCode) {
        String message = String.format("[Recipia] 인증번호[%s]를 입력해주세요.", verificationCode);

        PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

        try {
            // fixme: 테스트할때는 일단 제외 (비용 문제)
//            PublishResponse response = tokyoSnsClient().publish(request);
//            log.info("Sent message {} to {} with messageId {}", message, phoneNumber, response.messageId());
            log.info("Sent message {} to {}", message, phoneNumber);
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
        }
    }

}
