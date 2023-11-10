package com.recipia.member.config.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.plugins.EC2Plugin;
import com.amazonaws.xray.plugins.ECSPlugin;
import com.amazonaws.xray.strategy.sampling.LocalizedSamplingStrategy;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import jakarta.servlet.Filter;
import com.amazonaws.xray.jakarta.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.strategy.jakarta.SegmentNamingStrategy;


import java.net.URL;

@Configuration
public class AwsXrayConfig {

    /**
     * 이 코드는 AWS X-Ray SDK를 초기화한다.
     * EC2 및 ECS 플러그인을 활성화하고 sampling-rules.json 파일에 정의된 샘플링 규칙을 사용한다.
     */
    // static 블록은 클래스가 JVM에 로드될 때 단 한 번 실행된다. 이곳에서 AWS X-Ray 레코더를 설정하고 초기화한다.
    static {

        /**
         * AWSXRayRecorderBuilder는 AWS X-Ray 레코더 인스턴스를 구성하기 위한 빌더 패턴 클래스다.
         * standard() 메소드는 기본 구성으로 빌더를 생성한다.
         * withPlugin 메소드를 사용하여 EC2 및 ECS 플러그인을 레코더에 추가한다.
         * 이 플러그인들은 AWS X-Ray가 EC2 인스턴스와 ECS 컨테이너에서 실행 중인 애플리케이션에 대한 추가적인 정보를 수집하도록 한다.
         */
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
                .withPlugin(new EC2Plugin())
                .withPlugin(new ECSPlugin());

//        URL ruleFile = AwsXrayConfig.class.getResource("/sampling-rules.json");
//        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));

        AWSXRay.setGlobalRecorder(builder.build());
    }

    /**
     * 이 빈은 들어오는 HTTP 요청을 필터링하고 AWS X-Ray를 사용하여 추적하는 데 사용된다.
     * 필터는 "springbootService"라는 이름을 사용하여 추적되는 서비스를 식별한다.
     */
//    @Bean
//    public AWSXRayServletFilter TracingFilter() {
//        return new AWSXRayServletFilter("recipia-member");
//    }

    @Bean
    public Filter TracingFilter() {
        return new AWSXRayServletFilter(SegmentNamingStrategy.dynamic("Scorekeep"));
    }

}
