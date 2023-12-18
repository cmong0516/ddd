package com.example.ddd.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderNo implements Serializable {

    // OrderNo 는 Order Entity 의 식별자.
    // 식별자로 사용할 밸류 타입은 Serializable 인터페이스를 상속해야한다.
    // 밸류 타입을 식별자로 사용할때의 장점은 식별자에 기능을 추가할수 있다는 점.
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
