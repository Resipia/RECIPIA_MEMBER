package com.recipia.member.config.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorder;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.entities.Segment;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@EnableAspectJAutoProxy
@Configuration
public class AwsXrayConfig {

    // 애플리케이션 시작 시 X-Ray를 초기화
    @Bean
    public AWSXRayRecorder awsXRayRecorder() {
        return AWSXRayRecorderBuilder.standard().build();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new XRayInterceptor());
        return restTemplate;
    }

    // HTTP 요청을 위한 X-Ray 필터
    @Bean
    public Filter xrayFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {

                // X-Ray 세그먼트 시작
                Segment segment = AWSXRay.beginSegment(request.getRequestURI());

                try {
                    filterChain.doFilter(request, response);
                } finally {
                    // X-Ray 세그먼트 종료
                    AWSXRay.endSegment();
                }
            }
        };
    }

}
