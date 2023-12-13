package com.example.ddd.order.domain;


// 엔티티의 가장 큰 특징은 식별자를 가진다는 것이다.
// 식별자는 엔티티 객체마다 고유해서 각 엔티티는 서로 다른 식별자를 갖는다.
// 주문 1 과 주문 2 는 서로 다른 식별자를 가진다.
// 엔티티의 식별자는 내부 속성이 바뀌더라도 유지되고 엔티티 생성시 함께 생성 , 삭제시 함께 삭제된다.
// 엔티티를 구현한 캘르스는 식별자를 이용해서 equals() , hashCoce() 를 재정의 할수 있다.

// 엔티티의 식별자를 생성하는 시점.
// 엔티티의 식별자를 생성하는 시점은 도메인의 특징과 사용하는 기술에 따라 달라진다.
// 1. 특정 규칙에 따라 생성 (현재 시간과 다른 값을 조합 , 단 주의할 점은 동시에 식별자를 생성해도 같은 값이 존재하면 안된다.)
// 2. UUID , NanoId 와 같은 고유 식별자 생성기 사용
// 3. 값을 직접 입력 (아이디 또는 이메일)
// 4. 일련번호 사용(DB가 제공하는 AI 를 사용한다. 이 방법은 DB에 데이터를 삽입해야 값을 알수 있기 때문에 테이블에 데이터를 추가하기 전에는 식별자를 알수 없다.)
public class Order {
    private List<OrderLine> orderLines;
    private ShippingInfo shippingInfo;
    private Money totalAmounts;
    private OrderState orderState;
    private String orderNumber;

    public Order(List<OrderLine> orderLines , ShippingInfo shippingInfo) {
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    private void setOrderLines(List<OrderLine> orderLines) {
        verifyAtLeastOneOrMoreOrderLines(orderLines);
        this.orderLines = orderLines;
        calculateTotalAmounts();
    }

    private void setShippingInfo(ShippingInfo shippingInfo) {
        // shippingInfo 가 null 이면 Exception 이 발생하기 때문에 ShippingInfo 는 필수다.
        if (shippingInfo == null) {
            throw new IllegalArgumentException("no ShippingInfo");
        }
        this.shippingInfo = shippingInfo;
    }

    private void verifyAtLeastOneOrMoreOrderLines(List<OrderLine> orderLines) {
        if (orderLines == null || orderLines.isEmpty()) {
            throw new IllegalArgumentException("no OrderLine");
        }
    }

    private void calculateTotalAmounts() {
        int sum = orderLines.stream()
                .mapToInt(x -> x.getAmounts())
                .sum();

        this.totalAmounts = new Money(sum);
    }

    public void changeShippingInfo(ShippingInfo shippingInfo) {
        verifyNotYetShipped();
        setShippingInfo(shippingInfo);
    }

    public void cancel() {
        verifyNotYetShipped();
        this.orderState = OrderState.CANCELED;
    }

    private void verifyNotYetShipped() {
        if (orderState != OrderState.PAYMENT_WAITING && orderState != OrderState.PREPARING) {
            throw new IllegalStateException("already shipped");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != Order.class) {
            return false;
        }
        Order other = (Order) obj;
        if (this.orderNumber == null) {
            return false;
        }
        return this.orderNumber.equals(other.orderNumber);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
        return result;
    }
}
