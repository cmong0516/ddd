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