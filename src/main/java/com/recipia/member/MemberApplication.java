package com.recipia.member;

import com.recipia.member.kafka.TopicLister;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(MemberApplication.class, args);
	}

	/**
	 * 애플리케이션 시작 시에 TopicLister의 listTopics() 메서드가 실행되어 Kafka 토픽 목록을 출력한다.
	 */
	@Bean
	public CommandLineRunner run(TopicLister topicLister) {
		return args -> {
			topicLister.listTopics();
		};
	}

}
