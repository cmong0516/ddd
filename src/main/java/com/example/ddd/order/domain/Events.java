package com.example.ddd.order.domain;

import org.springframework.context.ApplicationEventPublisher;

public class Events {
    private static ApplicationEventPublisher publisher;

    static void setPublisher(ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    public static void raise(Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }

    // Events 클래스의 raise() 는 ApplicationEventPublisher 가 제공하는 publishEvent() 메서드를 사용해서 이벤트를 발생시킴.
    // Events 클래스가 사용할 ApplicationEventPublisher 객체는 setPublisher() 로 셋.
}
