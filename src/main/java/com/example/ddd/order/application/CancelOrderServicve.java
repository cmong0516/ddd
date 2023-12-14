package com.example.ddd.order.application;

import com.example.ddd.order.domain.CancelPolicy;
import com.example.ddd.order.domain.Canceller;
import com.example.ddd.order.domain.Order;
import com.example.ddd.order.domain.OrderNo;
import com.example.ddd.order.domain.OrderRepository;
import com.example.ddd.order.exception.NoCancellablePermission;
import com.example.ddd.order.exception.NoOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderServicve {
    private OrderRepository orderRepository;
    private CancelPolicy cancelPolicy;

    public CancelOrderServicve(OrderRepository orderRepository, CancelPolicy cancelPolicy) {
        this.orderRepository = orderRepository;
        this.cancelPolicy = cancelPolicy;
    }

    @Transactional
    public void cancel(OrderNo orderNo, Canceller canceller) {
        Order order = orderRepository.findById(orderNo)
                .orElseThrow(() -> new NoOrderException());

        if (!cancelPolicy.hasCancellationPermission(order, canceller)) {
            throw new NoCancellablePermission();
        }
        order.cancel();
    }
    // 응용 서비스 로직을 직접 수행하기 보다는 도메인 모델에 로직 수행을 위임하여 Order에서 cancel() 을 실행한다.
    // 주문 도메인은 배송지 변경 , 결제 완료 주문 총액 계산 등의 핵심 로직을 도메인 모델에서 구현한다.

    // 인프라 스트럭쳐 영역은 구현 기술에 대한 것을 다루는데
    // RDBMS 연동처리 , 메시징 큐에 메세지를 전송,수신 , 몽고DB , Redis 와의 데이터 연동을 처리한다.

    // 표현 -> 응용 -> 도메인 -> 인프라스트럭처ㅈ
    // 이 구조는 상위 계층에서 하위 계층으로의 의존만 존재한다. (더 아래 계층으로도 가능)
}
