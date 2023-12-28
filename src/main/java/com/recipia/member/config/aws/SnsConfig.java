package com.recipia.member.config.aws;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


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
    private String snsTopicNicknameChangeARN;


}
