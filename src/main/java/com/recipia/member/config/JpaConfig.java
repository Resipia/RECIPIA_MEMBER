package com.recipia.member.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA의 Auditing기능을 활성화시킨다.
 */
@EnableJpaAuditing
@Configuration
public class JpaConfig {

}
