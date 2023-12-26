package com.example.ddd.order.domain;

import java.util.List;

public interface ProductRecommendationService {
    List<Product> getRecommendationOf(ProductId id);
}
