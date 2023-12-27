package com.example.ddd.order.domain;

import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventForwarder {
    private static final int DEFAULT_LIMIT_SIZE = 100;

    private EventStore eventStore;
    private OffsetStore offsetStore;
    private EventSender eventSender;
    private int limitSize = DEFAULT_LIMIT_SIZE;

    public EventForwarder(EventStore eventStore, OffsetStore offsetStore, EventSender eventSender) {
        this.eventStore = eventStore;
        this.offsetStore = offsetStore;
        this.eventSender = eventSender;
    }

    @Scheduled(initialDelay = 1000L, fixedDelay = 1000L)
    public void getAndSend() {
        long nextOffset = getNextOffset();
        List<EventEntry> events = eventStore.get(nextOffset,limitSize);

        if (!events.isEmpty()) {
            int processedCount = sendEvent(events);

            if (processedCount > 0) {
                saveNextOffset(nextOffset + processedCount);
            }
        }

    }

    private long getNextOffset() {
        return offsetStore.get();
    }

    private int sendEvent(List<EventEntry> events) {
        int processedCount = 0;
        try {
            for (EventEntry event : events) {
                eventSender.send(event);
                processedCount++;
            }
        } catch (Exception e) {

        }

        return processedCount;
    }

    private void saveNextOffset(long nextOffset) {
        offsetStore.update(nextOffset);
    }
}

// 자동 증가 칼럼 주의 사항
// 주요키로 자동 증가 칼럼을 사용할 때 자동 증가 칼럼은 insert 쿼리를 실행하는 시점에 값이 증가하지만
// 실제 데이터는 트랜잭션을 커밋하는 시점에 DB에 반영된다.
// insert 쿼리를 실행해서 자동증가 칼럼이 증가했더라도 트랜잭션을 커밋하기 전에 조회하면 증가한 값을 가진 레코드는 조회되지 않음.
