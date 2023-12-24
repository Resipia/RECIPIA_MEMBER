package com.recipia.member.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Import({TestSecurityConfig.class,TestZipkinConfig.class})   // 테스트 설정 클래스 적용
@SpringBootTest
@ActiveProfiles("test")
public abstract class TotalTestSupport {
}
