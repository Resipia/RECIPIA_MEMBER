package com.recipia.member.config.aws;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;


@Getter
@Configuration
public class SnsConfig {

    @Value("${spring.cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${spring.cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    @Value("${spring.cloud.aws.region.static}")
    private String awsRegion;

    @Value("${spring.cloud.aws.sns.topics.nickname-change}")
    private String nicknameChangeArn;

    @Value("${spring.cloud.aws.sns.topics.signUp}")
    private String signUpArn;

    @Value("${spring.cloud.aws.sns.topics.member-withdraw}")
    private String memberWithdrawArn;

    @Bean
    public SnsClient getSnsClient () {
        return SnsClient.builder()
                .region(Region.of(awsRegion)) // 리전 설정 추가
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(awsAccessKey, awsSecretKey)))
                .build();
    }

}
