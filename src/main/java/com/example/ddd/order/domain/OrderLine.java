package com.example.ddd.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

@Embeddable
public class OrderLine {
//    private Product product;
//    private int price;
//    private int quantity;
//    private int amounts;

    // Money 를 사용하게 변경

    @Embedded
    private ProductId productId;
    @Column(name = "price")
    private Money price;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "amounts")
    private Money amounts;

}
