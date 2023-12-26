package com.example.ddd.order.domain;

import java.util.List;

public class RecSystemClient implements ProductRecommendationService{
    private ProductRepository productRepository;

    @Override
    public List<Product> getRecommendationOf(ProductId id) {
        List<RecommendationItem> items = getRecItems(id.getValue());
        return toProducts(items);
    }

    private List<RecommendationItem> getRecItems(String itemId) {
        return externalRecClient.getRecs(itemId);
    }

    private List<Product> toProducts(List<RecommendationItem> items) {
        return items.stream()
                .map(item -> toProductId(item.getItemId()))
                .map(productId -> productRepository.findById(productId))
                .toList();
    }

    private ProductId toProductId(String itemId) {
        return new ProductId(itemId);
    }
}
