package com.recipia.member.config.aws;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Subsegment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class XRayAspect {

    @Around("execution(* org.springframework.web.client.RestTemplate.*(..))")
    public Object traceRestTemplate(ProceedingJoinPoint pjp) throws Throwable {
        // X-Ray 서브세그먼트 시작
        Subsegment subsegment = AWSXRay.beginSubsegment("RestTemplate");
        try {
            // 요청 실행
            return pjp.proceed();
        } catch (Exception e) {
            AWSXRay.getCurrentSegment().addException(e);
            throw e;
        } finally {
            // X-Ray 서브세그먼트 종료
            AWSXRay.endSubsegment();
        }
    }

}
