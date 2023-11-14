package com.recipia.member.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EventRecordListener {

    /**
     * 이벤트를 호출한 서비스 코드의 트랜잭션과 묶이게 된다.
     */
    @Transactional
    @EventListener
    public void listen(NicknameChangeEvent event) {
        // 여기서 db에 저장하는 로직 실행 (트랜잭션이 묶여있어야 함)
    }

}
