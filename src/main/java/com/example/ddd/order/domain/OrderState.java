package com.example.ddd.order.domain;


// 주문 상태를 나타내는 Enum
public enum OrderState {
    PAYMENT_WAITING, PREPARING,SHIPPED,DELIVERING,DELIVERY_COMPLETED,CANCELED;
}
