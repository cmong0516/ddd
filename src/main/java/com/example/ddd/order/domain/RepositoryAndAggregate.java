package com.example.ddd.order.domain;

public class RepositoryAndAggregate {
    // 애그리거트는 개념상 완전한 한개의 도메인 모델을 표현하므로 객체의 영속성을 처리하는 리포지터리는 애그리거트 단위로 존재한다.
    // 애그리거트가 개념적으로 하나이므로 리포지터리는 애그리거트 전체를 DB에 영속화 해야한다.
    // 애그리거트를 구하는 리포지터리 메서드는 완전한 애그리거트를 제공해야 한다.

    public class Order {
        private Orderer orderer;
    }

    public class Orderer {
        private Member member;
    }

    // 이렇게 구현할 경우 order.getOrderer.getMember().getId() 로 쉽게 조회가 가능하다.
    // 하지만 서비스가 커짐에 따라 DB가 나뉘어져 이런식의 조회를 사용할수 없다면 어떻게 할것인가??
    // 또 한 애그리거트 내부를 다른 애그리거트 에서 접근이 가능하다는 것은 변경할수도 있다는 것이다.
    // 한 애그리거트 에서 다른 애그리거트 의 상태를 변경하는 것은 애그리거트 간의 의존 결합도를 높이고 변경을 어렵게 만든다.
    // 이것을 해결하기 위해 ID를 이용해 다른 애그리거트를 참조하는 방법을 사용하자.

    public class Order {
        private Orderer orderer;
    }

    public class Orderer {
        private MemberId memberId;
    }

    public class Member {
        private MemberId id;
    }

    // ID를 참조하면서 모든 객체가 참조로 연결되지 않았다.
    // 필요할 경우 id 를 사용해 조회한다면 지연로딩과 같은 결과를 낼수 있다.
    // 또 애그리거트 별로 다른 기술을 사용하는것도 가능해진다.
    // 하지만 id 를 사용한 조회에서는 조회 속도에 문제가 생길수 있다.
    // 예를들어 주문 목록을 보여줘야 한다면 각 주문마다 상품과 회원 애그리거트를 읽어와야 하는데
    // 주문개수가 10개 라면 주문을 일거옹기 위한 1번의 쿼리와 주문별로 각 상품을 읽어오기 위한 10번의 쿼리를 실행한다. ( N+1 )
    // 많은 쿼리를 실행하기 때문에 조회 속도가 느려진다.
    // 이를 해결하기 위해서는 조회 전용 쿼리를 사용해야 한다.
}
