package com.example.ddd.order.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;

public interface MemberDataDao extends Repository<MemberData, String> {
    List<MemberData> findByNameLick(String name, Pageable pageable);

    // Pageable 타입은 인터페이스로 실제 Pageable 타입 객체는 PageRequest 클래스를 이용해서 생성한다.

    // EX) PageRequest pageRequest = PageRequest.of(1,10);
    List<MemberData> user = memberDataDao.findByNameLike("사용자%", pageRequest);
    // PageRequest.of() 메서드는 (페이지 번호, 한 페이지의 개수)
    // 페이지 번호는 0부터 시작한다.

    Page<MemberData> findByBlocked(boolean blocked, Pageable pageable);

    // Page 타입을 사용하면 데이터 목록 뿐만 아니라 조건에 해당하는 전체 개수도 구할수 있다
    // Pageable 을 사용하는 메서드의 리턴 타입이 Page 일 경우 Spring Data JPA 는 목록 조회 쿼리와 함께 Count 쿼리도 실행한다.
    // Count 쿼리가 필요하지 않다면 불필요한 쿼리를 실행하게 되는 Page 로 받지 말고 List 로 받자.
    // 스펙을 사용하는 findAll() 의 경우 Pageable 타입을 사용하면 리턴 타입이 Page가 아니여도 Count 쿼리를 실행한다.


}
