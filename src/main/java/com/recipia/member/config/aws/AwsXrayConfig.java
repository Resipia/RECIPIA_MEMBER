package com.recipia.member.config.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.jakarta.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

@Configuration
public class AwsXrayConfig {

    /**
     * 이 코드는 AWS X-Ray SDK를 초기화한다.
     * EC2 및 ECS 플러그인을 활성화하고 sampling-rules.json 파일에 정의된 샘플링 규칙을 사용한다.
     */
    static {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
                .withPlugin(new EC2Plugin())
                .withPlugin(new ECSPlugin());

        URL ruleFile = AwsXrayConfig.class.getResource("/sampling-rules.json");
        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

        AWSXRay.setGlobalRecorder(builder.build());
    }

    /**
     * 이 빈은 들어오는 HTTP 요청을 필터링하고 AWS X-Ray를 사용하여 추적하는 데 사용된다.
     * 필터는 "springbootService"라는 이름을 사용하여 추적되는 서비스를 식별한다.
     */
    @Bean
    public AWSXRayServletFilter TracingFilter() {
        return new AWSXRayServletFilter("recipia-member");
    }

}
