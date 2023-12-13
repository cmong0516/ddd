package com.example.ddd.order.domain;


// 밸류 타입인 Money
public class Money {
    private int value;

    public Money(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    // 밸류타입의 장점 : 밸류 타입을 위한 기능을 추가할수 있다.

    public Money add(Money money) {
        return new Money(this.value + money.value);
    }

    public Money multiply(int multiplier) {
        return new Money(value * multiplier);
    }

    // Money 를 사용한다면 정수연산이 아니라 돈계산 이라는 의미로 해석할수 있다.
    // 밸류 타입을 사용하면 코드의 의미를 조금더 명확하게 알수 있다.
    // 밸류 객체는 데이터를 변경할때 기존 데이터를 변경 하기 보다는 새로운 데이터를 값으로 갖는 밸류 객체를 생성하는 방식을 선호한다.
    // -> 불변객체로 만들수 있다.
}
