package com.example.ddd.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.time.LocalDateTime;


// hibernate 는 JPA 확장 기능으로 @Subselect 를 제공한다.
// @Subselect 는 쿼리 결과를 @Entity 로 매핑할수 있다.
// @Immutable , @Subselect , @Synchronize 는 하이버네티으 전용 애너테이션 인데 이 태그를 사용하면 테이블이 아닌 쿼리 결과를 @Entity 로 매핑할수 있다.

// @Subselect 는 조회 쿼리를 값으로 갖는다.
// 하이버네이트 는 이 select 쿼리의 결과를 매핑할 테이블처럼 사용한다.
// DBMS가 여러 테이블을 조인해서 조회한 결과를 한 테이블처럼 보여주기 위한 용도로 뷰를 사용하는 것처럼 @Subselect 를 사용하면 쿼리 실행 결과를 매핑할 테이블처럼 사용한다.

// 뷰를 수정할수 없듯이 @Subselect 로 조회한 @Entity 역시 수정할수 없다.
// 실수로 매핑 필드를 수정하면 하이버네이트는 변경 내역을 반영하는 update 쿼리를 실행한다.
// 이때 매핑한 테이블이 없으므로 에러가 발생하는데 이런 문제를 방지하기 위해 @Immutable 을 사용한다.
// @Immutable 을 사용하면 하이버네이트는 해당 엔티티의 매핑 필드/ 프로퍼티 가 변경되어도 DB에 반영하지 않고 무시함.

// Order 를 조회한 후에 ShippingInfo 를 변경하면 @Immutable 어노테이션 으로 인해 update 쿼리가 실행되지 않아 DB의 데이터와 ShippingInfo 를 변경한 Order 의 값이 달라진다.
// DB 내부에서 최신 데이터가 아니라 이전 데이터를 가지고 있는것.
// 이 문제를 해결하기 위해 @Synchronize 를 사용한다.
// @Synchronize 는 해당 엔티티와 관련된 테이블 목록을 명시하는데 하이버 네이트는 엔티티를 로딩하기 전에 지정한 테이블과 관련된 변경이 발생하면 flush 를 먼저 한다.
// 따라서 OrderSummary 를 로딩하는 시점에서는 변경 내역이 반영된다.
@Entity
@Immutable
@Subselect(
        """
        select o.order_number as number,
        o.version,
        o.orderer_id,
        o.orderer_name,
        o.total_amounts,
        o.receiver_name,
        o.state,
        o.order_date,
        p.product_id,
        p.name as product_name
        from purchase_order o inner join order_line ol
            on o.order_number = ol.order_number
            cross join product p
        where
        ol.line_idx = 0
        and ol.product_id = p.product_id"""
)
@Synchronize({"purchase_order", "order_line", "product"})
public class OrderSummary {

    @Id
    private String number;
    private long version;
    @Column(name = "orderer_id")
    private String ordererId;
    @Column(name = "orderer_name")
    private String ordererName;
    @Column(name = "total_amounts")
    private int totalAmounts;
    @Column(name = "receiver_name")
    private String receiverName;
    private String state;
    @Column(name = "order_date")
    private LocalDateTime orderDate;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "product_name")
    private String productName;

    protected OrderSummary() {
    }

    public String getNumber() {
        return number;
    }

    public long getVersion() {
        return version;
    }

    public String getOrdererId() {
        return ordererId;
    }

    public String getOrdererName() {
        return ordererName;
    }

    public int getTotalAmounts() {
        return totalAmounts;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getState() {
        return state;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }
}
