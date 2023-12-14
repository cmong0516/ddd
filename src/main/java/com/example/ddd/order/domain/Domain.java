package com.example.ddd.order.domain;

public class Domain {
    // 엔티티 : 고유의 식별자를 가지는 객체로 자신의 라이프 사이클을 갖는다.
        // 주문,결제,회원,상품 과 같이 도메인의 고유한 개념을 표현하며 도메인 모델의 데이터를 포함하고 해당 데이터와 관련된 기능을 함께 제공.

    // 밸류 : 고유의 식별자를 갖지 않는 객체로 주로 개념적으로 하나인 값을 표현할때 사용. (ex 주소 필드들을 묶어 Address)
    // 애그리거트 : 연간된 엔티티와 밸류 객체를 기념적으로 하나로 묶은것.
        // Order Entity , OrderLine 밸류 , Orderer 밸류 모두는 주문 애그리거트로 묶을수 있다.
    // 리포지터리 : 도메인 모델의 영속성을 처리한다.
    // 도메인 서비스 : 특정 엔티티에 속하지 않는 도메인 로직을 제공한다.
        // 할인 금액 계산 을 보면 상품 , 쿠폰 , 회원등급 , 구매금액 등 여러 조건을 이용해서 구현하게 되는데
        // 도메인 로직이 여러 엔티티와 밸류를 필요로 하면 도메인 서비스에서 로직을 구현한다.



    // DB테이블의 엔티티와 도메인 모델의 엔티티는 다르다.
    // 두 모델의 가장 큰 차이점은 도메인 모델의 엔티티는 데이터와 함께 도메인 기능을 제공한다.
    // 예를들어 주문을 표현하는 엔티티는 주문과 관련된 데이터 뿐만 아니라 배송지 주소 변경을 위한 기능을 제공한다.

    public class OrderSample {
        private OrderNo number;
        private Orderer orderer;
        private ShippingInfo shippingInfo;

        public void changeShippingInfo(ShippingInfo shippingInfo) {

        }
    }
}
