package com.recipia.member.config;

import brave.sampler.Sampler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ZipkinTracingConfig {

    private static final List<String> ALLOWED_PATHS = Arrays.asList(
            "/member/nicknameChange"    // 허용할 첫 번째 URL 패턴
//            "/api/another",    // 허용할 두 번째 URL 패턴
//            "/api/more"        // 추가로 허용할 URL 패턴
    );

    @Bean
    public Sampler customSampler() {
        return new Sampler() {
            @Override
            public boolean isSampled(long traceId) {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

                if (requestAttributes instanceof ServletRequestAttributes) {
                    HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
                    return ALLOWED_PATHS.stream().anyMatch(path -> request.getRequestURI().startsWith(path));
                }

                // HTTP 요청 정보가 없는 경우 기본적으로 추적하지 않음
                return false;
            }
        };
    }
}
