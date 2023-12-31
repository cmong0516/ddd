package com.example.ddd.order.domain;

public class CQRS {
    // CQRS 란 Command 명령 모델과 Query 조회 모델을 분리하는 패턴이다.
    // 명령 모델은 상태를 변경하는 기능을 구현할때 사용하고 조회 모델은 데이터를 조회하는 기능을 구현할때 사용한다.
    // 예를들어 회원가입 , 암호변경 , 주문취소 처럼 상태를 변경하는 기능을 구현할 때는 명령 모델을 사용한다.
    // 주문목록 , 주문 상세 내역 처럼 데이터를 직접 보여주는 기능을 구현할 때는 조회 모델을 사용한다.

    // 엔티티 , 애그리거트 , 리포지터리 등 앞에서 살펴본 모델들은 상태를 변경할때 주로 사용되는 것들이다.
    // 조회 기능에서 주로 사용하는 것들은 정렬 ,페이징 , 검색조건 지정 등이다.
}
