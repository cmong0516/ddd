package com.example.ddd.order.domain;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface OrderDataDao {
    Optional<OrderData> findById(OrderNo id);

    List<OrderData> findByOrderer(String orderId, Date fromDate, Date toDate);


    // 검색 조건을 다양하게 조합해야 할때 사용할수 있는 것이 스펙 이다.
    // 스펙은 애그리거트가 특정 조건을 충족하는지를 검사할때 사용하는 인터페이스다.

    public interface Speficiantion<T>{
        public boolean isSatisfiedBy(T agg);
        // 검사 대상이 되는 객체 agg
        // 스펙을 리포지토리에 사용하면 agg 는 애그리거트 루트가 되고 스펙을 DAO 에서 사용하면 agg는 검색 결과로 리턴할 데이터 객체가 된다.
    }

    // Order 애그리거트 객체가 특정 고객의 주문이 맞는지 확인하는 스펙.
    public class OrderSpec implements Speficiantion<Order> {
        private String orderId;

        public OrderSpec(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public boolean isSatisfiedBy(Order agg) {
            return agg.getOrderId().getMemberId().getId().equals(orderId);
        }
    }
    // 리포지터리나 DAO 는 검색 대상을 걸러내는 용도로 스펙을 사용한다.
    // 만약 리포지터리가 메모리에 모든 애그리거트를 보관하고 있다면
    // ex)  findAll().stream().filter(order -> spec.isSatisfiedBy(order))
    // ex2) Specitication<Order> orderSpec = new OrderSpec("hello");
    // List<Order> orders = orderRepository.findAll(orderSpec);

    // 하지만 실제론 애그리거트 객체가 모두 메모리에 보관되어 있지 않고 그렇게 해서도 성능에 좋지 않다.

}
