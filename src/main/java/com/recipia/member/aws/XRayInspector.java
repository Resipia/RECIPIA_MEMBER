package com.recipia.member.aws;

import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


import com.amazonaws.xray.spring.aop.BaseAbstractXRayInterceptor;
import com.amazonaws.xray.entities.Subsegment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
public class XRayInspector extends BaseAbstractXRayInterceptor {

    /**
     * X-Ray 활성화 클래스에 대한 포인트컷을 정의한다.
     * 여기서는 @XRayEnabled 어노테이션이 붙은 클래스와 *Controller로 끝나는 빈에 적용된다.
     */
    @Override
//    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled) && bean(*Controller)")
    @Pointcut("@within(com.amazonaws.xray.spring.aop.XRayEnabled)")
    protected void xrayEnabledClasses() {
        // 포인트컷을 정의하는 메서드는 본문이 비어있어야 함.
    }

    /**
     * X-Ray 서브세그먼트에 메타데이터를 추가하는 메서드를 오버라이드한다.
     * @param proceedingJoinPoint 진행 중인 조인 포인트
     * @param subsegment 생성된 서브세그먼트
     * @return 메타데이터 맵
     */
    @Override
    protected Map<String, Map<String, Object>> generateMetadata(ProceedingJoinPoint proceedingJoinPoint, Subsegment subsegment) {
        // 여기에서 커스텀 메타데이터를 생성하고 추가할 수 있다.
        // 예를 들어, 요청 매개변수나 특정 필드 값을 메타데이터로 추가할 수 있다.
        Map<String, Map<String, Object>> metadata = super.generateMetadata(proceedingJoinPoint, subsegment);
        // 메타데이터 추가 예시:
        // metadata.put("myKey", Map.of("value", "myValue"));
        return metadata;
    }

}
