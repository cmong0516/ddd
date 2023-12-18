package com.example.ddd.order.domain;


// 엔티티의 가장 큰 특징은 식별자를 가진다는 것이다.
// 식별자는 엔티티 객체마다 고유해서 각 엔티티는 서로 다른 식별자를 갖는다.
// 주문 1 과 주문 2 는 서로 다른 식별자를 가진다.
// 엔티티의 식별자는 내부 속성이 바뀌더라도 유지되고 엔티티 생성시 함께 생성 , 삭제시 함께 삭제된다.
// 엔티티를 구현한 캘르스는 식별자를 이용해서 equals() , hashCoce() 를 재정의 할수 있다.

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import java.util.List;

// 엔티티의 식별자를 생성하는 시점.
// 엔티티의 식별자를 생성하는 시점은 도메인의 특징과 사용하는 기술에 따라 달라진다.
// 1. 특정 규칙에 따라 생성 (현재 시간과 다른 값을 조합 , 단 주의할 점은 동시에 식별자를 생성해도 같은 값이 존재하면 안된다.)
// 2. UUID , NanoId 와 같은 고유 식별자 생성기 사용
// 3. 값을 직접 입력 (아이디 또는 이메일)
// 4. 일련번호 사용(DB가 제공하는 AI 를 사용한다. 이 방법은 DB에 데이터를 삽입해야 값을 알수 있기 때문에 테이블에 데이터를 추가하기 전에는 식별자를 알수 없다.)

@Entity
@Table(name = "purchase_order")
public class Order {

    @EmbeddedId
    // 밸류 타입을 엔티티의 식별자로 사용하려면 @Id 대신 @EmbeddedId를 사용한다.
    private OrderNo number;
    private Orderer orderer;
    // 관계형 데이터 베이스 (RDBMS)에서는 밸류 타입을 제대로 표현하기가 힘들다.
    // Orderer 을 저장하려면 개별 데이터를 저장하거나 별도의 테이블로 분리해야한다.
    @ElementCollection(fetch = FetchType.EAGER)
    // 엔티티의 일부로 저장 되어야 하는 기본 객체의 컬렉션을 지정하는데 사용된다.
    @CollectionTable(name = "order_line", joinColumns = @JoinColumn(name = "order_number"))
    // 엔티티 클래스의 특정 컬렉션을 데이터베이스에 매핑할때 사용되며 구체적으로 컬렉션을 저장할 테이블과 테이블의 관계를 정의한다.
    // order_line 은 컬렉션을 저장할 테이블의 이름.
    @OrderColumn(name = "line_idx")
    // 리스트나 배열 과 같은 순서가 있는 컬렉션의 순서를 유지하기 위한 컬럼을 정의할때 사용된다.
    // line_idx 는 순서 정보를 저장할 칼럼의 이름 이며 이 컬럼은 컬렉션 내 요소의 순서를 나타내며 각 요소가 컬렉션에 추가도니 순서대로 값을 갖는다.
    private List<OrderLine> orderLines;
    private ShippingInfo shippingInfo;
    private Money totalAmounts;
    private OrderState orderState;
    private String orderNumber;

//    public Order() {
//    }

    public Order(List<OrderLine> orderLines , ShippingInfo shippingInfo) {
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    public Order(Orderer orderer, List<OrderLine> orderLines, ShippingInfo shippingInfo, OrderState orderState) {
        setOrderer(orderer);
        setOrderLines(orderLines);
        setShippingInfo(shippingInfo);
    }

    public void setOrderer(Orderer orderer) {
        if (orderer == null) {
            throw new IllegalArgumentException("no orderer");
        }

        this.orderer = orderer;
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

    public OrderNo getId() {
        return id;
    }

    public void setSHippingInfo(ShippingInfo shippingInfo) {

    }

    public void setOrderState(OrderState orderState) {

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

    //public static void main(String[] args) {
//        Order order = new Order();

        // set 메서드를 마구 사용한다면

//        order.setOrderLines(lines);
//        order.setShippingInfo(shippinginfo);

//        order.setOrderState(OrderState.PREPARING);

        // 위 코드를 보면 주문자 Orderer 를 설정하지 않고도 주문 완료 처리가 가능하게 변경할수 있다.
        // setter 는 이렇게 도메인에서 의도하지 않은 동작을 수행할수 있게 하기에 사용하지 말도록 하자.
    //}

    // setter 를 사용하지 말자고 해놓고 위에 수정본에선 사용했다. 하지만 다른점이 있다.
    // 접근제어자를 public -> private 으로 하여 외부에서 변경하지 못하도록 하였다.



    // DTO : Data Transfer Object 의 약자로 프레젠테이션 계층과 도메인 계층이 데이터를 주고받을때 사용하는 구조체.
    // DTO는 도메인 로직을 담고 있지는 않기에 get/set 을 사용해도 괜찮다.

}
