package com.example.ddd.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderNo implements Serializable {

    @Column(name = "order_number")
    private String number;

    protected OrderNo() {

    }

    public OrderNo(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderNo orderNo = (OrderNo) obj;
        return Objects.equals(number, orderNo.number);
    }

    public static OrderNo of(String number) {
        return new OrderNo(number);
    }
}
