package com.recipia.member.event;

import com.recipia.member.aws.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class SnsPublishListener {

    private final SnsService snsService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(NicknameChangeEvent event) {
        log.info("닉네임 변경 이벤트 발행(SNS) - [멤버 pk : {}]", event.memberId());
        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("memberId", event.memberId());
        snsService.publishNicknameToTopic(messageMap);
    }




}
