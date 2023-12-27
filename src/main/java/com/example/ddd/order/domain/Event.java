package com.example.ddd.order.domain;

import org.springframework.context.event.EventListener;

public class Event {
    // 이벤트란 과거에 일어난일.
    // 도메인 모델에 이벤트를 도입하려면 네개의 구성요소인
        // 이벤트
        // 이벤트 생성 주체
        // 이벤트 디스패처
        // 이벤트 핸들러
    // 를 구현해야 한다.

    // 이벤트 생성 주체
    // 도메인 모델에서 이벤트 생성 주체는 엔티티 ,밸류 , 도메인 서비스와 같은 도메인 객체.

    // 이벤트 핸들러
    // 이벤트 핸들러는 이벤트 생성 주체가 발생한 이벤트에 반응한다.
    // 이벤트 핸들러는 생성 주체가 발생한 이벤트를 전달받아 이벤트에 담긴 데이터를 이용해서 원하는 기능을 실행.

    // 이벤트 디스패처
    // 이벤트 생성 주체와 이벤트 핸들러를 연결해줌.

    public class ShippingInfoChangedEvent{
        // 이벤트는 과거에 일어난일 이기 때문에 Changed 라고 이름을 지었다.
        private String orderNumber;
        private long timestamp;
        private ShippingInfo newShippingInfo;
    }

    // 이 이벤트가 발생하는 주체는 Order 애그리거트 이다.

    public class ShippingInfoChangedHandler {
        @EventListener(ShippingInfoChangedEvent.class)
        public void handle(ShippingInfoChangedEvent event) {
            Order order = orderRepository.findById(event.getOrderNo());


        }
    }


    // 10.2.3 이벤트는 크게 두가지 용도로 쓰인다.

    // 1. 트리거
        // 도메인의 상태가 바뀔때 다른 후처리가 필요하면 후처리기를 실행하기 위한 트리거로 이벤트를 사용.
        // 시스템 간의 데이터 동기화.


    // 10.2.4 이벤트의 장점

    // 구매 취소를 할때 환불 로직이 포함되어야 하는데 이벤트를 사용해서 취소 로직에서 환불 로직을 없앨수 있다.
    // 환불 실행 로직은 주문 취소 이벤트를 받는 이벤트 핸들러로 이동하여 주문 도메인에서 결제 도메인으로의 의존을 제거.
    // 기능 확장
        // 구매 취소시 환불과 함께 이메일로 취소 내용을 보내고 싶다면 이메일 발송을 처리하는 핸들러를 구현하면 된다.

    // 10.3 이벤트, 핸들러, 디스패처

    // 이벤트 클래스 : 이벤트를 표현한다.
    // 디스패처 : 스프링이 제공하는 ApplicationEventPublisher 를 이용하다.
    // Events : ApplicationEventPublisher 를 이용해서 이벤트를 발행한다.
    // 이벤트 핸드러 : 이벤트를 수신해서 처리한다.

}