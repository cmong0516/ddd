package com.example.ddd.order.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;

public interface Repository Repository {
    // 애그리거트를 어떤 저장소에 저장하느냐 에 따라 리포지토리를 구현하는 방법은 다르다.
    // 도메인 모델과 리포지터리를 JPA를 사용해서 구현해보자.
    // 리포지토리는 인프라스트럭처 영역에 해당한다.
    // 인터페이스를 나누는 기준은 애그리거트 루트이다.
    // JPA를 사용한 리포지터리는 JPA 의 EntityManager em 을 이용해서 구현한다.
    // 기본적인 메서드 들은 Spring Data Jpa 를 통해 구현되어 있으니 이를 사용하는게 좋다.
    // delete 메서드의 경우 실제로 데이터를 삭제하는 경우는 많지 않다.
    // 활성화 등의 필드를 둬서 이것을 노출 시질지 말지 여부를 결정하게 된다.

    // 엔티티와 밸류 기본 매핑 구현.
    // 애그리거트 루트는 엔티티 이므로 @Entity 어노테이션으로 매핑 설정을 한다.
    // 밸류 타입은 @Embeddable 로 매핑 설정을 한다.
    // 밸류타입 프로퍼티는 @Embadded로 매핑 설정 한다.
    // EX) 주문 객체인 Order 는 루트 엔티티 이므로 @Entity
    // 주문의 필드인 Orderer 는 객체를 @Embeddable 로 감싸고 Order 필드 Orderer 는 @Embedded.

    // 프로퍼티와 매핑되는 칼럼 이름이 다를경우 @AttributeOverrides 어노테이선을 사용해서 매핑할 칼럼 이름을 설정한다.


    @Embeddable
    public class ShippingInfo {
        @Embedded
        @AttributeOverrides({@AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zipcode")),
                @AttributeOverride(name = "address1", column = @Column(name = "shipping_addr1")),
                @AttributeOverride(name = "address2", column = @Column(name = "shipping_addr2"))})
        private Address address;

        @Column(name = "shipping_message")
        private String message;

        @Embedded
        private Receiver receiver;
    }

    // 위에서도 밸류 타입인 Receiver 은 @Embedded.


    @Entity
    public class Order{
        @Embedded
        private Orderer orderer;

        @Embedded
        private ShippingInfo shippingInfo;
    }

    // 밸류타입은 모두 @Embedded.


    // JPA 에서 @Entity 와 @Embeddable 로 클래스를 매핑하려면 기본 생성자를 제공해야 한다.
    // @Embeddable 이 붙은 밸류타입 객체의 기본 생성자를 추가해준다.
    // 이 기본 생서자는 JPA 프로바이더가 객체를 생성할때만 사용되므로 protected 로 선언하여 다른 객체에서 접근하지 못하게 한다.


}
