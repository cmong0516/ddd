package com.example.ddd.order.domain;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class DiscountCalculationService {
    // 도메인 서비스
    // 여러 애그리거트의 기능을 사용하여 한 애그리거트에 책임을 주기 애매할때 사용.

    public Money calculateDiscountAmounts(List<OrderLine> orderLines, List<Coupon> coupons, MemberGrade memberGrade) {
        Money couponDiscount = coupons.stream().map(coupon -> calculateDiscountAmounts(coupon))
                .reduce(Money(0), (v1, v2) -> v1.add(v2));
        // 각 쿠폰을 돌면서 총 쿠폰의 할인금액을 더해준다.

        Money membershipDiscount = calculateDiscountAmounts(orderer.getMember().getGrade());
        // 멤버의 등급을 매개변수로 할인금액을 계산하는 메서드를 실행.

        return couponDiscount.add(membershipDiscount);
        // 총 할인금액 반환.
    }

    // 할인 서비스를 사용하는 주체는 애그리거트가 도리수도 있고 응용 서비스가 도리수도 있다.
    // 할인 서비스를 애그리거트이 결제 금액 계산 기능에 전달하면 주체는 그 애그리거트.

    // * 도메인 서비스 객체를 애그리거트에 주입하지 않기.

    // 애그리거트의 메서드를 실행할때 도메인 서비스 객체를 파라미터로 전달한다는 것은 애그리거트가 도메인 서비스에 의존한다는 것이다.
    // 스프링 DI , AOP 를 공부하다 보면 애그리거트가 의존하는 도메인 서비스를 의존주입으로 처리하고 싶을수 있다.
    // 의존 주입을 하기 위해 애그리거트 루트 엔티티에 도메인 서비스에 대한 참조 필드를 추가했다고 치자.

    public class Order {
        @Autowired
        private DiscountCalculationService discountCalculationService;
    }

    // 도메인 객체는 필드로 구성된 데이터와 메서드를 이용해서 개념적으로 하나인 모델을 표현한다.
    // 모델의 데이터를 담는 필드는 모델에서 중요한 고성요소 인데 주입한 discountCalculationService 는 데이터 자체와는 관련이 없다.
    // 또 Order 객체를 DB에 보관할때 다른 필드와 달리 저장 대상도 아니다.

    // 특정 기능이 응용 서비스 인지 도메인 서비스 인지 판단하는 기준은 애그리거트의 상태를 변경하느냐 이다.
    // 애그리거트의 상태를 변경한다 -> 도메인 로직.
    // 도메인 로직 이면서 한 애그리거트에 넣기 적합하지 않다 -> 도메인 서비스.
}
