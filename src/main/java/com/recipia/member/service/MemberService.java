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

@Slf4j // SLF4J 로깅 사용 선언
@Service // 이 클래스가 서비스 컴포넌트임을 선언
@RequiredArgsConstructor // final이나 @NonNull 필드에 대한 생성자를 자동 생성
public class MemberService {

    private final MemberRepository memberRepository; // 멤버 리포지토리
    private final KafkaTemplate<String, String> kafkaTemplate; // Kafka 템플릿

    // 멤버를 찾는 메서드
    public MemberDto findMember(String username) {
        // DB에서 멤버 정보를 찾고, 없다면 예외를 발생시킴
        MemberDto memberDto = memberRepository.findMemberByUsername(username)
                .map(MemberDto::fromEntity)
                .orElseThrow(() -> new MemberApplicationException(ErrorCode.USER_NOT_FOUND));

        // Kafka로 멤버 업데이트 이벤트를 전송
        sendMemberUpdatedEvent(memberDto);

        return memberDto;
    }

    // 멤버 업데이트 이벤트를 Kafka에 전송하는 메서드
    // 스프링3부터 ListenableFuture가 deprecated되어 CompletableFuture를 사용하면서 callback이 사라져서 whenComplete()로 콜백을 처리하도록 바뀜
    public CompletableFuture<Void> sendMemberUpdatedEvent(MemberDto memberDto) {
        // 비동기 작업을 위해 CompletableFuture를 사용
        return CompletableFuture.runAsync(() -> {
                    try {
                        // Kafka에 "member-updated" 토픽으로 메시지 전송
                        kafkaTemplate.send("member-updated", memberDto.username()).get();
                        log.debug("[kafka] member-updated 이벤트 발행, 업데이트한 유저 아이디 : {}", memberDto.username()); // 성공 로그
                    } catch (Exception e) {
                        log.error("Failed to send message: {}", e.getMessage()); // 실패 로그
                    }
                }, Executors.newCachedThreadPool()) // 쓰레드 풀 설정
                .whenComplete((result, ex) -> { // 작업 완료 후 콜백
                    if (ex != null) {
                        log.error("An exception occurred: {}", ex.getMessage()); // 예외 발생시 로그
                    } else {
                        log.debug("[kafka] member-updated 이벤트 발행완료"); // 성공시 로그
                    }
                });
    }
}



