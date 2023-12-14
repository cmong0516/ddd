package com.example.ddd.order.domain;

public class Receiver {
    private String name;
    private String phoneNumber;

    public Receiver(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Receiver)) {
            return false;
        }
        Receiver that = (Receiver) obj;
        return this.name.equals(that.name) && this.phoneNumber.equals(that.phoneNumber);
    }
}

// 두 밸류 객체를 비교할 때는 모든 속성의 값이 같은지 비교해야 한다.

// 엔티티의 식별자의 실제 데이터는 String 과 같은 문자열로 구성된 경우가 많다.
// ex) 신용카드 번호, 이메일
// 주문번호 같은 경우 orderNo 밸류 타입을 식별자로 사용하면 해당 필드가 주문번호 라는것을 명확히 나타낼수 있다.