package com.example.ddd.order.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@Service
public class OrderCanceledEventHandler {
    private RefundService refundService;

    public OrderCanceledEventHandler(RefundService refundService) {
        this.refundService = refundService;
    }

    @EventListener(OrderCanceledEvent.class)
    public void handle(OrderCanceledEvent event) {
        refundService.refund(event.getOrderNumber());
    }
}

// 도메인 기능 실행
// 도메인 기능은 Events.raise() 로 이벤트 발생
// Events.raise() 는 ApplicationEventPublisher 를 이용해서 이벤트를 출판
// ApplicationEventPublisher 는 @EventListener 어노티에션이 붙은 메서드를 찾아 실행.

// 10.4 동기 이벤트 처리 문제

// 이벤트를 사용하여 취소 로직에서 환불 로직이 포함되어 결합도가 높아지는 문제를 해결했다.
// 하지만 외부 서비스에 영향을 받는다. -> 외부 서비스의 성능저하가 내 시스템의 성능저하로 이어진다.
// 또 트랜잭션의 문제가 있다. refundService.refund() 에서 익셉션이 발생하면 (환불이 실패하면) cancel() 메서드를 롤백할것인가 ??


// 10.5 비동기 이벤트 처리

// A 가 이루어졌을때 B가 항상 바로 실행될 필요가 없을수도 있다. 또 B 가 실패하더라도 재시도하여 일정시간 내에 성공하면 될수도 있다.
// A 가 발생했을때 별도 스레드로 B 를 수행하는 핸들러를 실행하는 방식.

// 1. 로컬 핸들러를 비동기로 실행
// 2. 메세지 큐를 사용하기
// 3. 이벤트 저장소와 이벤트 포워더 사용하기
// 4. 이벤트 저장소와 이벤트 제공 API 사용하기


// 1. 로컬 핸들러 비동기 실행
// @EnableAsync 어노테이션을 사용해서 비동기 기능을 활성화
// 이벤트 핸들러 메서드에 @Async 어노테이션을 붙인다.


@SpringBootApplication
@EnableAsync
public class ShopApplication{
    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}


@Service
public class OrderCanceledEventHandler {
    @Async
    @EventListener(OrderCanceledEvent.class)
    public void handle(OrderCanceledEvent event) {
        refundService.refund(event.getOrderNumber());
    }
}

// 스프링은 OrderCanceledEvent 가 발생하면 handle() 메서드를 별도 스레드에서 비동기로 실행.


// 2. 메세지 큐를 사용하기
// 카프카 , 래빗MQ 와 같은 메시징 시스템을 사용하는것.
// 메시지 큐는 이벤트를 메시지 리스너에게 전달하고 메세지 리스너는 알맞은 이벤트 핸들러를 이용해서 이벤트를 처리한다,
// 이때 이벤트를 메시지 큐에 저장하는 과정과 메시지 큐에서 이벤트를 읽어와 처리하는 과정은 별도 스레드나 프로세스로 처리된다.

// 카프카 : 글로벌 트랜잭션을 지원하진 않지만 높은 성능
// 래빗MQ : 글로벌 트랜잭션 지원과 고가용성을 지원하여 안정적으로 메시지를 전달할수 있다.


// 3 이벤트 저장소와 이벤트 포워더 사용하기.

// 이벤트르 비동기로 처리하는 방법중에 DB에 저장한 뒤에 별도 프로그램을 이용해서 이벤트 핸들러에 전달할수 있다.
// 1. 이벤트 발생
// 2. 스토리지에 이벤트를 저장
// 3. 포워더는 주기적으로 이벤트 저장소에서 이벤트를 가져와 이벤트 핸들러를 실행

// 이 방식은 도메인의 상태와 이벤트 저장소로 동일한 DB 를 사용한다. 즉 도메인의 상태 변화와 이벤트의 저장이 로컬 트랜잭션으로 처리.
// 이벤트를 물리적 저장소에 보관하기 때문에 핸들러가 이벤트 처리에 실패할 경우 포워더는 다시 이벤트를 저장소에서 읽어와 핸들러를 실행.

// API 방식과 포워더 방식의 차이점은 이벤트를 전달하는 방식에 있다.
// 포워더 : 포워더를 이용해서 이벤트를 외부에 전달
// API : 외부 핸들러가 API 서버를 통해 이벤트 목록을 가져간다.