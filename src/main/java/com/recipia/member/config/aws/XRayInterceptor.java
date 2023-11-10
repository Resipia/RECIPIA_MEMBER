package com.recipia.member.config.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class XRayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // X-Ray 서브세그먼트 시작
        Subsegment subsegment = AWSXRay.beginSubsegment("RestTemplate");
        try {
            // HTTP 요청 실행
            return execution.execute(request, body);
        } catch (Exception e) {
            AWSXRay.getCurrentSegment().addException(e);
            throw e;
        } finally {
            // X-Ray 서브세그먼트 종료
            AWSXRay.endSubsegment();
        }
    }

}
