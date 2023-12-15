package com.example.ddd.order.domain;

public class Domain {
    // 엔티티 : 고유의 식별자를 가지는 객체로 자신의 라이프 사이클을 갖는다.
        // 주문,결제,회원,상품 과 같이 도메인의 고유한 개념을 표현하며 도메인 모델의 데이터를 포함하고 해당 데이터와 관련된 기능을 함께 제공.

    // 밸류 : 고유의 식별자를 갖지 않는 객체로 주로 개념적으로 하나인 값을 표현할때 사용. (ex 주소 필드들을 묶어 Address)
        // 밸류 객체는 불변으로 구현하는것이 좋다. -> 데이터 변경이 생길경우 새로운 객체를 반환.
    // 애그리거트 : 연간된 엔티티와 밸류 객체를 기념적으로 하나로 묶은것.
        // Order Entity , OrderLine 밸류 , Orderer 밸류 모두는 주문 애그리거트로 묶을수 있다.
        // 도메인 모델이 복잡해지면 개발자가 전체 구조가 아닌 한개의 엔티티와 밸류에만 집중하는 상황이 발생하는데 전체 모델의 관계와 개별 모델을 이해하는데 도움을 줌.
    // 리포지터리 : 도메인 모델의 영속성을 처리한다.
        // 도메인 객체를 지속적으로 사용하기위해 물리적인 저장소에 도메인 객체를 보관해야 하는데 이를 위한 도메인 모델
        // 애그리거트 단위로 도메인 객체들을 저장하고 조회하는 기능을 한다.
    // 도메인 서비스 : 특정 엔티티에 속하지 않는 도메인 로직을 제공한다.
        // 할인 금액 계산 을 보면 상품 , 쿠폰 , 회원등급 , 구매금액 등 여러 조건을 이용해서 구현하게 되는데
        // 도메인 로직이 여러 엔티티와 밸류를 필요로 하면 도메인 서비스에서 로직을 구현한다.



    // DB테이블의 엔티티와 도메인 모델의 엔티티는 다르다.
    // 두 모델의 가장 큰 차이점은 도메인 모델의 엔티티는 데이터와 함께 도메인 기능을 제공한다.
    // 예를들어 주문을 표현하는 엔티티는 주문과 관련된 데이터 뿐만 아니라 배송지 주소 변경을 위한 기능을 제공한다.
    // 또다른 차이점 으로는 도메인 모델의 엔티티는 두개 이상의 데이터가 개념적으로 하나인 경우 밸류 타입을 이용해서 표현할수 있다.
    public class OrderSample {
        private OrderNo number;
        private Orderer orderer;
        private ShippingInfo shippingInfo;

        public void changeShippingInfo(ShippingInfo shippingInfo) {

        }
    }

    // 도메인에 따른 패키지 구성
    // 회원 기능은 member , 주문 기능은 order
    // 각 하위 패키지에는 command.application , command.domain , infra, query , ui등이 있다.
    // ex
    // - src
    //  - main
    //    - java
    //      - com.example.domain
    //        - model
    //        - repository
    //        - service
    //      - com.example.application
    //        - service
    //      - com.example.infrastructure
    //        - repository
    //      - com.example.interfaces
    //        - web
    //        - api


    // 도메인의 service 와 application 의 service 는 무엇이 다른가 ?

    // domain 의 service : 도메인 모델 내의 비지니스 로직을 수행.
    // application 의 service : 클라이언트의 요청을 처리하고 도메인 서비스 및 도메인 모델을 활용하여 작업을 조정.

    // 주문을 예로 들면 도메인의 서비스 에서 주문 도메인과 관련된 처리를 하고 application 의 서비스 에서 이를 사용한다.
}
