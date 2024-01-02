package com.recipia.member.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Around("execution(* com.recipia.member.adapter.in.web..*.*(..))")
    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();

            String formattedStartTime = dateFormat.format(new Date(startTime));
            String formattedEndTime = dateFormat.format(new Date(endTime));

            log.debug(" ===== Method: " + joinPoint.getSignature().getName() + " ===== ");
            log.debug(" ===== Parameters: " + Arrays.toString(joinPoint.getArgs()) + " ===== ");
            log.debug(" ===== Start Time: " + formattedStartTime + " ===== ");
            log.debug(" ===== End Time: " + formattedEndTime + " ===== ");
            log.debug(" ===== Duration: " + (endTime - startTime) + "ms ===== ");
        }
    }


}
