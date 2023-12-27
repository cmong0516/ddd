package com.example.ddd.order.domain;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventApi {
    private EventStore eventStore;

    public EventApi(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @GetMapping("/api/events")
    public List<EventEntry> list(@RequestParam("offset") Long offset, @RequestParam("limit") Long limit) {
        return eventStore.get(offset, limit);
    }
}


// 가장 ㅏㅁ지막에 처리한 데이터의 offset 인 lastOffset을 구한다. ,없으면 0
// 마지막에 처리한 lastOffset 을 offset 을 사용해서 API를 실행한다.
// API 결과로 받은 데이터를 처리한다.
// offset + 데이터 개수를 lastOffset 으로 저장한다.

// 마지막에 처리한 lastOffset 을 저장하는 이유는 같은 이벤트를 중복해서 처리하지 않기위해서.