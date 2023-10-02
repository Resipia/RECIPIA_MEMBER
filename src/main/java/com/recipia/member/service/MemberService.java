package com.recipia.member.service;

import com.recipia.member.dto.MemberDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    /**
     * 멤버를 찾는 메서드
     */
    public MemberDto findMember(String username) {
        MemberDto memberDto = memberRepository.findMemberByUsername(username)
                .map(MemberDto::fromEntity)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        sendMemberUpdatedEvent(memberDto); // Kafka로 멤버 업데이트 이벤트를 전송

        return memberDto;
    }

    /**
     * 멤버 업데이트에 대한 kafka 이벤트 발행
     */
    public String memberUpdateEventPublish(String username) {
        MemberDto memberDto = memberRepository.findMemberByUsername(username)
                .map(MemberDto::fromEntity)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        sendMemberUpdatedEvent(memberDto).join();  // 비동기 작업 완료를 기다림
        return memberDto.username();
    }

    /**
     * 멤버 업데이트 이벤트를 Kafka에 전송한다.
     * KafkaTemplate의 send 메서드를 사용하여 메시지를 전송한 후, 결과를 handleKafkaResponse 메서드에 위임합니다.
     * Spring 3 이후로 ListenableFuture가 deprecated 되어 CompletableFuture를 사용한다.
     */
    public CompletableFuture<Void> sendMemberUpdatedEvent(MemberDto memberDto) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("member-updated", memberDto.username());
        return handleKafkaResponse(future, memberDto.username());
    }

    /**
     * Kafka로 메시지를 성공적으로 보냈는지에 대한 결과를 처리한다.
     * 로그를 남기거나 예외 상황을 처리하는 등의 후처리 작업을 담당한다.
     *
     * @param future 메시지 전송 결과를 담은 CompletableFuture 객체
     * @param username 업데이트한 유저의 아이디
     * @return 후처리 작업에 대한 CompletableFuture<Void> 객체
     */
    private CompletableFuture<Void> handleKafkaResponse(CompletableFuture<SendResult<String, String>> future, String username) {
        return future.thenAccept(result -> {
            log.debug("[kafka] member-updated 이벤트 발행, 업데이트한 유저 아이디 : {}", username);
        }).exceptionally(ex -> {
            log.error("Failed to send message: {}", ex.getMessage());
            return null;
        });
    }

}



