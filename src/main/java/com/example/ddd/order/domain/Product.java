package com.example.ddd.order.domain;

import com.example.ddd.order.domain.Repository.MoneyConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @EmbeddedId
    private ProductId id;
    private String name;

    @Convert(converter = MoneyConverter.class)
    private Money price;
    private String detail;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "productId")
    @OrderColumn(name = "list_idx")
    private List<Image> images = new ArrayList<>();

    public void changeImage(List<Image> newImages) {
        images.clear();
        // @Entity 에 대해서 clear()를 실행하면 select 를 실행하여 대상 entity를 로딩하고 각 개별 엔티티에 대해 delete 쿼리를 실행한다.
        // image 목록을 가져오기 위한 쿼리 1번 + 각각의 delete 쿼리를 실행하여 많은 수의 쿼리가 발생하게 된다.
        // 하이버네트 에서 @embeddable 타입의 컬렉션에선 clear() 메서드를 실행하면 위의 과정을 거치지 않고 한번의 delete 쿼리로 삭제해준다.
        // 지금 clear() 메서드의 경우는 전자의 경우로 images 에 4개의 이미지가 들어있었다면 조회쿼리 1번 + delete 쿼리 4번 총 5번의 쿼리가 발생한다.
        // 이 문제를 해결하려면 Image 인터페이스를 상속하고있는 상화을 포기하고 @Embeddable 로 매핑된 단일 클래스로 구현해야 한다.
        images.addAll(newImages);
    }

    // @Embeddable 을 사용한 Image

    @Embeddable
    public class Image {
        @Column(name = "iamge_type")
        private String imageType;
        @Column(name = "image_path")
        private String path;

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "upload_time")
        private Date uploadTime;

        public boolean hasThumbnail() {
            if (imageType.equals("II")) {
                return true;
            }
            return false;
        }
    }

}
