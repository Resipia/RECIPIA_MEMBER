package com.recipia.member.config.batch;

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.recipia.member.aws.SnsService;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.dto.SnsMessageDto;
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
public class BatchConfiguration extends DefaultBatchConfiguration {

    private final SnsService snsService;
    private final EntityManagerFactory entityManagerFactory;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;


    // 배치 작업에서 사용할 chunkSize 값 설정. 기본값은 1000
    @Value("${chunkSize:2}")
//    @Value("${chunkSize:1000}")
    private int chunkSize;
    @Value("${pageSize:4}")
    private int pageSize;

    // 상수 정의
    private static final String TRACE_ID = "traceId";
    private static final String MEMBER_ID = "memberId";


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
     * ItemProcessor를 설정하여 MemberEventRecord를 필터링하고 isBatch를 true로 설정하고 snsService.publishNicknameToTopic()를 호출.
     * 이때 publishNicknameToTopic안의 트랜잭션이 시작되고 ItemWriter의 동작까지 같은 트랜잭션으로 묶어진다.
     */
    @Bean
    public ItemProcessor<MemberEventRecord, MemberEventRecord> itemProcessor() {
        log.info("ItemProcessor 동작!");

        return item -> {
            try {
                processItem(item);
            } catch (JsonProcessingException e) {
                log.error("JSON 처리 중 오류 발생: {}", e.getMessage(), e);
                // 예외 처리 로직 추가 (필요에 따라)
            }
            return item;
        };

    }


    private void processItem(MemberEventRecord item) throws JsonProcessingException {

        log.info("processItem 동작중");

        // publishNicknameToTopic 메소드에 전달할 메시지 변수 선언
        String newJsonMessage = "";

        // 1. 이벤트 객체의 attribute를 memberid, traceid 를 지닌 메시지 dto로 변환
        SnsMessageDto snsMessageDto = objectMapper.readValue(item.getAttribute(), SnsMessageDto.class);

        // 2. snsMessageDto에 traceid 있는지 확인
        if (snsMessageDto.traceId() != null) {
            // 2-1. traceid 있으면 바로 sns publish
            // attribute JSON 파싱 및 새로운 JSON 문자열 생성
            newJsonMessage = createJsonMessage(snsMessageDto);
        } else {
            // 2-2. traceid 없으면 새로 생성해주고 메시지에 담아서 sns publish

            // 새로운 Span 생성
            Span newSpan = tracer.newTrace();
            // 새로운 traceId 추출
            String newTraceId = newSpan.context().traceIdString();

            SnsMessageDto newSnsMessageDto = SnsMessageDto.of(newTraceId, snsMessageDto.memberId());


            // 새로운 JSON 메시지 생성
            newJsonMessage = createJsonMessage(newSnsMessageDto);

            // Span 종료
            newSpan.finish();

            // 2-3. 새로 생성된 traceid를 추가해서 attribute를 업데이트
            item.changeAttribute(newJsonMessage);

        }

        // 3. 배치로 재발행된 이벤트는 is_batch = true로 업데이트
        item.changeIsBatchTrue();

        // 4. SNS 발행 메소드
        snsService.publishNicknameToTopic(newJsonMessage);
    }

    ;

//        // 1. 이벤트 객체의 attribute를 memberid, traceid 를 가진 메시지 dto로 변환
//        SnsMessageDto snsMessageDto = objectMapper.readValue(item.getAttribute(), SnsMessageDto.class);
//
//        // 2. snsMessageDto에 traceid 있는지 확인
//        if (snsMessageDto.traceId() == null) {
//            // 2-1. traceid 없으면 새로 생성해주고 메시지에 담아서 sns publish
//
//            // 새로운 Span 생성
//            Span newSpan = tracer.newTrace();
//            // 새로운 traceId 추출
//            String newTraceId = newSpan.context().traceIdString();
//            // 새로운 JSON 객체 생성
//            ObjectNode jsonNode = objectMapper.createObjectNode();
//            jsonNode.put(TRACE_ID, newTraceId);
//            jsonNode.put(MEMBER_ID, item.getMember().getId().toString());
//            // 새로 생성된 traceid를 추가해서 attribute를 업데이트
//            item.changeAttribute(objectMapper.writeValueAsString(jsonNode));
//            // 새로운 Span 종료
//            newSpan.finish();
//        }
//
//        // attribute JSON 파싱 및 새로운 JSON 문자열 생성
//        String newJsonMessage = createJsonMessage(item.getAttribute());
//
//        // is_batch = true로 업데이트
//        item.changeIsBatchTrue();
//
//        // SNS 발행 메소드
//        snsService.publishNicknameToTopic(newJsonMessage);


    /**
     * [EXTRACT METHOD] - DB안의 데이터를 다시 parsing해서 뽑아낸 다음 그대로 다시 새로운 json으로 만드는 메서드
     * <p>
     * 이렇게 다시 가공을 안하고 바로 이벤트 저장소 테이블에 저장된 attribute 값을 파라미터로 넣어서 SNS를 발행했더니
     * SqsListener에서 메시지를 꺼내서 사용할 때 JSON PARSING 에러가 발생해서 이렇게 json 재가공을 해준것이다.
     */
//    private String createJsonMessage(String json) throws JsonProcessingException {
//        ObjectNode jsonNode = (ObjectNode) objectMapper.readTree(json);
//        ObjectNode newJsonNode = objectMapper.createObjectNode();
//        newJsonNode.put(TRACE_ID, jsonNode.get(TRACE_ID).asText());
//        newJsonNode.put(MEMBER_ID, jsonNode.get(MEMBER_ID).asText());
//        return newJsonNode.toString();
//    }
    private String createJsonMessage(SnsMessageDto snsMessageDto) throws JsonProcessingException {
        ObjectNode newJsonNode = objectMapper.createObjectNode();
        newJsonNode.put(TRACE_ID, snsMessageDto.traceId());
        newJsonNode.put(MEMBER_ID, snsMessageDto.memberId());
        return newJsonNode.toString();
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

}
