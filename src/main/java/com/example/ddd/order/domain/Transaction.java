package com.example.ddd.order.domain;

import com.example.ddd.order.domain.RepositoryAndAggregate.Member;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public class Transaction {
    // 트랜잭션 범위는 작을수록 좋다.
    // 한 트랜잭션이 한개의 테이블을 수정할 때와 세개의 테이블을 수정할 때의 성능차이가 많이 난다.
    // 한 트랜잭션 에서는 하나의 애그리거트 만을 수정해야 한다.
    // 만약 부득이하게 한 트랜잭션으로 두개 이상의 애그리거트를 수정해야 한다면 응용 서비스에서 두 애그리거트를 수정하도록 구현한다.

    // 8.1 애그리거트와 트랜잭션

    // 주문 애그리거트에 대해 운영자는 배송 상태로 변경할때 사용자가 배송지 주소를 변경하면 어떻게 될까 ??

    // 운영자
    // 주문 애그리거트 구함 -> 배송 상태로 변경 -> 트랜잭션 커밋

    // 사용자
    // 주문 애그리거트 구함 -> 배송지 변경 -> 트랜잭션 커밋

    // 사용자가 배송지를 변경하고 운영자가 작업을 했다면 문제가 발생하지 않을것이다.
    // 하지만 운영자가 배송 상태로 변경을 하고 사욪아가 배송지를 변경했다면 잘못된 결과가 나올것이다.
    // 애그리거트의 일관성을 유지하기 위해 두가지 방법이 있다.

    // 1. 운영자는 배송지 정보를 조회하고 상태를 변경하는 동안 고객이 애그리거트를 수정하지 못하게 한다.
    // 2. 운영자가 배송지 정보를 조회한 이후 고객이 정보를 변경하면 운영자가 애그리거트를 다시 조회한뒤 수정하도록 한다.


    // 8.2 선점 잠금

    // 선점 잠금 이란 먼저 애그리거트를 구한 스레드가 애그리거트 사용이 끝날 때까찌 다른 스레드가 해당 애그리거트를 수정하지 못하게 하는것.
    // 위의 예시에서 1 번 예에 해당할 것이다.
    // 애그리거트를 사용중인 스레드가 사용을 마칠때까지 다른 스레드가 접근할수 없으니 데이터 변경으로 인한 충돌이 발생하지 않을것이다.
    // 선점 잠금은 보통 DBMS 가 제공하는 행단위 잠금을 사용해서 구현한다.
    // JPA EntityManager 는 LockModeType 을 인자로 받는 find() 메서드를 제공한다.
    // EX) entityManager.find(Order.class, orderNo, LockModeType.PESSIMISTIC_WRITE);

    // JPA 프로바이더와 DBMS 에 따라 잠금 모드 구현이 다르다.
    // 하이버네트의 경우 PESSIMISTIC_WRITE 를 잠금모드로 사용하면 'for update' 쿼리를 이용해서 선점 잠금을 구현한다.
    // 스프링 데이터 JPA 는 @Lock 에너테이션을 사용해서 잠금 모드를 지정한다.

//    public interface MemberRepository extends Repository<Member, MemberId> {
//        @Lock(LockModeType.PESSIMISTIC_WRITE)
//        Optional<Member> findByIdForUpdate(MemberId memberId);
//    }

    // 8.2.1 선점 잠금의 교착상태.

    // 1. 스레드1 : A 애그리거트에 대한 선점 잠금 구함
    // 2. 스레드2 : B 애그리거트에 대한 선점 잠금 구함
    // 3. 스레드1 : B 애그리거트에 대한 선점 잠금 시도
    // 4. 스레드2 : A 애기르거트에 대한 선점 잠금 시도

    // 위 순서대로 진행된다면 스레드 1은 영원히 B 애그리거트에 대한 선점 잠금을 구할수 없다.
    // 스레드 1 : B 애그리거트의 선점 잠금을 시도하지만 스레드 2 가 이미 선점
    // 스레드 2 : A 애그리거트의 선점 잠금을 시도하지만 스레드 1 가 이미 선점
    // 두 스레드가 얽혀 다음 진행을 하지 못하는데 이런 상황을 교착상태 라고 한다.
    // 이런 교착 상태를 해결하기 위해서는 최대 대기 시간을 지정해야 한다
    // javax.persistence.lock.timeout 으로 밀리초 단위 최대 대기 시간을 설정하자.
    // 단 DBMS 에서 지원하는지 여부를 먼저 확인해야 한다.

    // 스프링 데이터 JPA 는 @QueryHints 에너테이션을 사용해서 쿼리 힌트를 지정할수 있다.

    // 8.3 비 선점 잠금
    // 비선점 잠금 은 동시에 접근하는 것을 막는 선점 잠금과 다르게 변경한 데이터를 실제 디비에 반영하는 시점에 변경 가능 여부를 확인하는 방법이다.
    // 비선점 잠금을 구현하려면 애그리거트에 버전으로 사용할 숫자 타입 프로퍼티를 추가해야 한다.
    // 애그리거트를 수정할 때마다 버전으로 사용하는 프로퍼티 값이 1씩 증가하는데 수정할 애그리거트와 매핑되는 테이블의 버전값이 동일한 경우에만 데이터를 수정한다.

    // JPA 에서는 @Version 어노테이션을 필드에 사용
    // @Version
    // private long version;


    // 8.3.1 강제 버전 증가

    // 애그리거트의 루트에 필드로 다른 엔티티가 존재할때 기능 실행 도중 루트가 아닌 다른 엔티티의 값만 변경된다고 하면 JPA 는 변경된 엔티티의 버전만 변경하고 루트 엔티티의 버전을 변경하지 않는다.
    // 만약 비 선점 잠금 방식으로 구현했다면 내부 필드 엔티티의 변경이 이루어져 루트 엔티티도 변경이 이루어져야 하지만 그렇지 않으면 오류가 발생한다.
    // JPA 는 이런 문제를 해결하기 위해 EntityManager.find() 메서드로 엔티티를 구할때 강제로 버전 값을 증가시키는 잠금 모드를 지원한다.
    // LockModeType.OPTIMISTIC_FORCE_INCREMENT 를 사용하면 된다.

}
