package com.example.ddd.order.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class OrdererIdSpec implements Specification<OrderSummary>{

    private String orderId;

    public OrdererIdSpec(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public Predicate toPredicate(Root<OrderSummary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        return cb.equal(root.get(OrderSummary_.ordererId), orderId);
    }
}
