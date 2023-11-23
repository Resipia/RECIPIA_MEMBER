package com.recipia.member.aws;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipia.member.domain.event.MemberEventRecord;
import com.recipia.member.dto.SnsInformationDto;
import com.recipia.member.dto.SnsMessageDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberEventRecordRepository;
import com.recipia.member.utils.MemberStringUtils;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * MEMBER 서버에서 SNS가 잘 발행되었을때 DB MEMBER_EVENT_RECORD테이블에 true로 값 업데이트 해주는 SQS
 * 단, Message 안에 traceId가 있을때만 정상 실행 되도록 구성
 * traceId 없을때는 에러로그만 남기기 (왜냐하면, 어차피 배치 작업을 통해 SNS 재발행 프로세스가 진행되기 때문.)
 */
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SqsEventRecordListener {

    private final ObjectMapper objectMapper;
    private final MemberEventRecordRepository memberEventRecordRepository;

    @SqsListener(value = "${spring.cloud.aws.sqs.record-sqs-name}")
    public void receiveSnsPublishedMessage(String messageJson) throws JsonProcessingException {

        SnsInformationDto snsInformationDto = objectMapper.readValue(messageJson, SnsInformationDto.class);
        SnsMessageDto snsMessageDto = objectMapper.readValue(snsInformationDto.Message(), SnsMessageDto.class);

        // 'traceId'가 null인지 확인
        if (snsMessageDto.traceId() == null) {
            log.error("No traceId found in the message. memberId: {}, skipping processing.", snsMessageDto.memberId());
            return;
        }

        String topicName = MemberStringUtils.extractLastPart(snsInformationDto.TopicArn());
        Long memberId = snsMessageDto.memberId();

        // todo: 여기서 지금 배치로 발행여부를 업데이트할때 published가 false인 것은 전부 true로 해줘야 할것같음 아니면 다른 조건문을 생각해봐야할것같음.
        //  왜냐하면 만약 4개가 false인채로 배치가 동작하면 얘는 그중 1개만 계속 true로 반복 수정해서 배치가 false인거는 계속 가져오다보니 멈추지 않고 무한반복함
        MemberEventRecord memberEventRecord = memberEventRecordRepository
                .findFirstByMember_IdAndSnsTopicOrderByIdDesc(memberId, topicName)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.EVENT_NOT_FOUND));

        memberEventRecord.changePublished();
    }
}
