package com.recipia.member.hexagonal.config.aop;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

/**
 * 해당 서버의 timezone을 Asia/Seoul로 설정하기 위한 클래스
 * LoggingAspect 클래스에서 시작 시간/종료 시간을 서울 시간으로 출력하기 위함
 */
@Configuration
public class TimeZoneConfig {

    @PostConstruct
    public void init(){
        // 서울 시간대로 설정
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }

}
