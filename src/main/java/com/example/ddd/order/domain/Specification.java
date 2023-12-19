package com.example.ddd.order.domain;


import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;

// 스프링 데이터 JPA 를 이용한 스펙 구현.
public interface Specification<T> extends Serializable {


    // Spring Data JPA 가 제공하는 스펙 인터페이스는 스펙을 조합할수 있는 and() , or() 두 메서드를 제공한다.
    // default Specification<T> and(@Nullable Specification<T> other) {...}
    // default Specification<T> or(@Nullable Specification<T> other) {...}

    // and() , or() 은 기본 구현을 가진 디폴트 메서드로
    // and() 는 두 스펙을 모두 충족하는 조건을 표현하는 스펙을 생성하고
    // or() 는 두 스펙중 하나 이상 충족하는 조건을 표현하는 스펙을 생성한다.




    @Nullable
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
}
