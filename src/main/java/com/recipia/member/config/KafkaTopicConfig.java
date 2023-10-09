package com.recipia.member.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka의 topic을 관리하는 클래스
 * todo: 앞으로는 producerm, consumer 두개의 클래스를 통해 관리해야 하므로 이 클래스는 수정이 필요하다.
 */
@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Kafka 관리자 설정을 Bean으로 등록한다. KafkaAdmin은 Spring Kafka 라이브러리에서 제공하는 클래스로,
     * Kafka의 AdminClient를 Spring 컨테이너에 쉽게 등록하고 관리할 수 있도록 도와준다.
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * Kafka AdminClient를 Bean으로 등록한다. AdminClient는 Kafka 클러스터를 프로그래밍 방식으로 관리할 수 있게 해주는 라이브러리이다.
     * 토픽 생성, 수정, 삭제 등의 작업을 수행할 수 있다.
     */
    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    /**
     * "member-updated" 라는 이름의 새 토픽을 생성한다. topic1() 메서드는 Spring 애플리케이션을 시작할 때 자동으로 호출된다.(@Bean 이라서)
     * 따라서 수동으로 호출할 필요는 없으며, "member-updated" 토픽은 애플리케이션 시작 시에 자동으로 생성된다.
     */
    @Bean
    public NewTopic topic1() {
        return new NewTopic("member-updated", 1, (short) 1);
    }

    /**
     * 토픽을 더 추가하려면 이렇게 Bean으로 등록한다.
     * 예시로 주석 처리되어 있다.
     */
//    @Bean
//    public NewTopic topic2() {
//        return new NewTopic("another-topic", 1, (short) 1);
//    }

}
