package com.example.ddd.order.domain;

import java.util.List;
import org.springframework.data.repository.Repository;
public interface OrderSummaryDao extends Repository<OrderSummary, String> {
    List<OrderSummary> findAll(Specification<OrderSummary> spec);

    // Specification<OrderSummary> spec = new OrdererIdSpec("user1");
    // List<OrderSummary> results = orderSummaryDao.findAll(spec);
}
