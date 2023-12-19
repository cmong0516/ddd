package com.example.ddd.order.domain;

import java.util.List;
import org.hibernate.mapping.SortableValue;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
public interface OrderSummaryDao extends Repository<OrderSummary, String> {
    List<OrderSummary> findAll(Specification<OrderSummary> spec);

    // Specification<OrderSummary> spec = new OrdererIdSpec("user1");
    // List<OrderSummary> results = orderSummaryDao.findAll(spec);

    // Spring Data JPA는 두가지 방법을 사용해서 정렬을 지정할수 있다.

    // 1. 메서드 이름에 OrderBy 를 사용해서 정렬 기준 지정
    // 2. Sort 를 인자로 전달

    List<OrderSummary> findByOrdererIdOrderByNumberDesc(String gordererId);
    // ordererId 프로퍼티 값을 기준으로 검색 조건 지정.
    // number 프로퍼티 값 역순으로 정렬.
    // findByOrdererIdOrderByOrderDateDescNumberAsc 처럼 두개 이상의 프로퍼티에 대한 정렬 순서도 지정할수 있다.
    // 위 메서드는 orderDate 프로퍼티를 기준으로 내림차순 정렬하고 Number 프로퍼티를 기준으로 오름차순 정렬한다.
    // 사용할수 있지만 메서드 이름이 너무 길어지는 단점이 존재한다.
    // 이럴땐 Sort 를 사용하자.


    List<OrderSummary> findByOrdererId(String ordererId, Sort sort);

    List<OrderSummary> findAllBy(Specification<OrderSummary> spec, Sort sort);

    // Spring Data Jpa 는 파라미터로 전달받은 Sort 를 사용해서 알맞게 정렬 쿼리를 생성한다.

    // JPQL 로 쿼리문을 작성해서 OrderView DTO로 값을 꺼내온다.
    // QueryDsl 의 @QueryProjection 을 사용해서 DTO 로 가져오는 것과 같은 기능을 하는것 같다.
    // 조회 전용 DTO 를 만드는 것은 표현 영역을 통해 사용자에게 데이터를 보여주기 위함.
}
