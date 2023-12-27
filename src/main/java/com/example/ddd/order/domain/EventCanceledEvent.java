package com.example.ddd.order.domain;

public class EventCanceledEvent {
    private String orderNumber;

    public EventCanceledEvent(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }
}
