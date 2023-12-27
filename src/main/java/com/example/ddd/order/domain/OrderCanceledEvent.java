package com.example.ddd.order.domain;

public class OrderCanceledEvent extends Event2{
    private String orderNumber;

    public OrderCanceledEvent(String orderNumber) {
        super();
        this.orderNumber = orderNumber;
    }
}
