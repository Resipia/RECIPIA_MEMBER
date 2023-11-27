package com.recipia.member.config.batch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.aws.SnsService;
import com.recipia.member.domain.event.MemberEventRecord;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * DefaultBatchConfiguration을 확장하는 방식은 스프링 배치 5에서 새로 도입된 접근 방식이다.
 * 이는 @EnableBatchProcessing의 기능을 포함하며, 기본 배치 인프라 구성을 제공한다.
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig extends DefaultBatchConfiguration {

    private final SnsService snsService;
    private final EntityManagerFactory entityManagerFactory;
    private final ObjectMapper objectMapper;


    // 배치 작업에서 사용할 chunkSize 값 설정. 기본값은 1000
    @Value("${chunkSize:2}")
//    @Value("${chunkSize:1000}")
    private int chunkSize;
    @Value("${pageSize:4}")
    private int pageSize;

    /**
     * Job을 정의. 하나 이상의 Step을 포함할 수 있으며, 여기서는 sendSmsStackStep을 시작점으로 설정함.
     */
    @Bean
    public Job job(final JobRepository jobRepository, final Step sendSmsStackStep) {
        return new JobBuilder("job", jobRepository)
                .start(sendSmsStackStep)
                .build();
    }

    /**
     * Step을 정의. 데이터 처리의 실질적인 단계를 구성함.
     * - reader: JpaPagingItemReader를 사용해 publishedAt이 false인 Member 데이터를 읽음
     * - writer: 읽어온 데이터를 SnsService를 통해 처리함
     */
    @Bean
    @Transactional
    public Step step(final JobRepository jobRepository,
                     final PlatformTransactionManager transactionManager) {
        return new StepBuilder("step", jobRepository)
                .<MemberEventRecord, MemberEventRecord>chunk(this.chunkSize, transactionManager)
                .reader(this.jpaPagingItemReader(entityManagerFactory))
                .processor(this.itemProcessor())
                .writer(this.itemWriter())
                .build();
    }

    /**
     * JpaPagingItemReader를 설정. publishedAt이 false인 Member 데이터를 조회하는 쿼리를 실행함.
     */
    @Bean
    public JpaPagingItemReader<MemberEventRecord> jpaPagingItemReader(EntityManagerFactory entityManagerFactory) {
        log.info("jpaPagingItemReader 동작!");
        JpaPagingItemReader<MemberEventRecord> reader = new JpaPagingItemReader<>();

        String jpql = """
                  SELECT mer
                  FROM MemberEventRecord mer
                  WHERE mer.published = false
                  AND mer.id IN (
                      SELECT MAX(innerMer.id)
                      FROM MemberEventRecord innerMer
                      WHERE innerMer.published = false
                      GROUP BY innerMer.member.id, innerMer.snsTopic
                  )
                  ORDER BY mer.id ASC
                """;

        reader.setQueryString(jpql);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(pageSize); // 페이지 크기 설정
        return reader;
    }

    /**
     * ItemProcessor를 설정하여 MemberEventRecord를 필터링하고 snsService.publishNicknameToTopic()를 호출.
     * 이때 publishNicknameToTopic안의 트랜잭션이 시작되고 ItemWriter의 동작까지 같은 트랜잭션으로 묶어진다.
     */
    @Bean
    public ItemProcessor<MemberEventRecord, MemberEventRecord> itemProcessor() {
        return item -> {
            log.info("processItem 동작중");

            switch (item.getSnsTopic()) {
                case "NicknameChange" -> snsService.publishNicknameToTopic(item.getAttribute(), item.getTraceId());
            }
            return item;
        };
    }

    /**
     * ItemWriter를 설정. 업데이트된 엔터티를 처리. 여기서 작업이 마무리되면 트랜잭션이 종료되고 커밋된다.
     */
    @Bean
    public ItemWriter<MemberEventRecord> itemWriter() {
        log.info("itemWriter 동작!");
        JpaItemWriter<MemberEventRecord> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    /**
     * [EXTRACT METHOD] - DB안의 데이터를 다시 parsing해서 뽑아낸 다음 그대로 다시 새로운 json으로 만드는 메서드
     */
//    private String createJsonMessage(SnsMessageDto snsMessageDto) throws JsonProcessingException {
//        ObjectNode newJsonNode = objectMapper.createObjectNode();
//        newJsonNode.put(MEMBER_ID, snsMessageDto.memberId().toString());
//        return newJsonNode.toString();
//    }


}
