package com.recipia.member.config;
import brave.sampler.Sampler;
import brave.sampler.SamplerFunction;
import brave.sampler.Matchers;
import brave.http.HttpRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

    @Bean
    public SamplerFunction<HttpRequest> myCustomSampler() {
        return new SamplerFunction<HttpRequest>() {
            @Override
            public Boolean trySample(HttpRequest request) {
                // 특정 URL 패턴을 추적에서 제외한다. 여기서는 "/health"로 시작하는 경로를 예로 든다.
                if (request.path().startsWith("/health")) {
                    return false; // 추적하지 않음
                }
                return true; // 그 외의 경우는 추적
            }
        };
    }
}
