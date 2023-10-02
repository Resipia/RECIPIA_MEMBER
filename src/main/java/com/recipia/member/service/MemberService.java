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
     * Kafka로 'member-updated' 이벤트를 전송하는 메서드.
     * send 메서드는 Kafka로 메시지 전송만 담당하고, 그 결과는 handleKafkaResponse에 위임한다. 이로써 단일 책임 원칙(SRP)을 준수한다.
     */
    public CompletableFuture<Void> sendMemberUpdatedEvent(MemberDto memberDto) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send("member-updated", memberDto.username());
        return handleKafkaResponse(future, memberDto.username());
    }

    /**
     * Kafka로 메시지를 성공적으로 보냈는지에 대한 결과를 처리한다.
     * 메시지 전송 성공시 로그를 남기고, 실패시 예외를 로깅한다. 이로써 단일 책임 원칙(SRP)을 준수한다.
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



