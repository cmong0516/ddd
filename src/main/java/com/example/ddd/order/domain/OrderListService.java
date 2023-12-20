package com.example.ddd.order.domain;

import java.util.List;

public class OrderListService {
    public List<OrderView> getOrderList(String ordererId) {
        return orderViceDao.selectByOrderer(ordererId);
    }

    // 서비스에서 수행하는 추가적인 로직이 없을뿐더러 단일 쿼리만 실행하는 조회 전용 기능이어서 트랜잭션이 필요하지 않다.
    // 이 경우라면 굳이 서비스를 만들 필요 없이 표현 영역에서 바로 조회 전용 기능을 사용해도 문제가 없다.
    // 응용 서비스가 사용자 요청 기능을 실행하는데 별다른 기여를 하지 못한다면 굳이 서비스를 만들지 말자.
}
