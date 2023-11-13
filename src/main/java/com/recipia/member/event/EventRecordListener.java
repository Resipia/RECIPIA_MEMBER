package com.recipia.member.event;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EventRecordListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(NicknameChangeEvent event) {
    }


}
