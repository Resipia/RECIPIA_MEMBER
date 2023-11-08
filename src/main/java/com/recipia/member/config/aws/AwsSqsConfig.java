package com.recipia.member.config.aws;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Getter
@Configuration
public class AwsSqsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    // SQS Client 세팅
    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return awsAccessKey;
                    }

                    @Override
                    public String secretAccessKey() {
                        return awsSecretKey;
                    }
                })
                .region(Region.of(awsRegion))
                .build();
    }

    // Listener Factory 설정 (Listener쪽에서만 설정하면 됨)
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory
                .builder()
                .sqsAsyncClient(sqsAsyncClient())
                .build();
    }

//    // 메세지 발송을 위한 SQS 템플릿 설정 (Sender쪽에서만 설정하면 됨)
//    @Bean
//    public SqsTemplate sqsTemplate() {
//        return SqsTemplate.newTemplate(sqsAsyncClient());
//    }


}
