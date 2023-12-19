package com.example.ddd.order.domain;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.time.LocalDateTime;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class OrderSummarySpecs {
    public static Specification<OrderSummary> ordererId(String orderId) {
        return (Root<OrderSummary> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.equal(root.<String>get("ordererId"), orderId);
    }

    public static Specification<OrderSummary> orderDateBetween(LocalDateTime from, LocalDateTime to) {
        return (Root<OrderSummary> root, CriteriaQuery<?> query, CriteriaBuilder cb) ->
                cb.between(root.get(OrderSummary_.orderDate), from, to);
    }

}
