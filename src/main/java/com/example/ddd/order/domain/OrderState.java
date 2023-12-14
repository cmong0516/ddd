package com.example.ddd.order.domain;


// 주문 상태를 나타내는 Enum
public enum OrderState {
    PAYMENT_WAITING, PREPARING,SHIPPED,DELIVERING,DELIVERY_COMPLETED,CANCELED;
    // 도메인에서 사용하는 용어는 의미를 나타내느데 매우 중요하다. 만약 이 enum 의 값들이 STEP1 , STEP2 , STEP3 등이였다면
    // 작성자 외에는 그 의미를 쉽게 파악할수 없었을것이다.
}
