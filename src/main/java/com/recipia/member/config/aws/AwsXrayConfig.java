package com.recipia.member.config.aws;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsXrayConfig {

//    static {
//        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard()
//                .withPlugin(new EC2Plugin())
//                .withPlugin(new ECSPlugin());
//
//        URL ruleFile = AwsXrayConfig.class.getResource("/sampling-rules.json");
//        builder.withSamplingStrategy(new LocalizedSamplingStrategy(ruleFile));
//
//        AWSXRay.setGlobalRecorder(builder.build());
//    }
//
//    @Bean
//    public Filter TracingFilter() {
//        return new AWSXRayServletFilter("springbootService");
//    }
}
