package com.recipia.member.service;

import com.recipia.member.dto.MemberDto;
import com.recipia.member.exception.ErrorCode;
import com.recipia.member.exception.MemberApplicationException;
import com.recipia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
     * 멤버 업데이트 이벤트를 Kafka에 전송하는 메서드
     * 스프링3부터 ListenableFuture가 deprecated되어 CompletableFuture를 사용하면서 callback이 사라져서 whenComplete()로 콜백을 처리하도록 바뀜
     */
    public CompletableFuture<Void> sendMemberUpdatedEvent(MemberDto memberDto) {
        return kafkaTemplate.send("member-updated", memberDto.username())
                .thenAccept(result -> log.debug("[kafka] member-updated 이벤트 발행, 업데이트한 유저 아이디 : {}", memberDto.username()))
                .exceptionally(ex -> {
                    log.error("Failed to send message: {}", ex.getMessage());
                    return null;
                });
    }

}



