package com.example.ddd.order.domain;

import jakarta.mail.Store;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountedCompleter;
import org.hibernate.query.Page;

public class Aggregate {
    // 애그리거트
    // 백개 이상의 테이블을 한 ERD 에 표시하게 되면 개별 테이블간의 관계를 파악하느라 큰 틀에서 데이터 구조를 이해하는데 어려움이 생긴다.
    // 도메인 객체 모델이 복잡해지면 개별 구성요소 위주로 모델을 이해하게 되고 전반적인 구조나 큰 수준에서 도메인 간의 관계를 파악하기 어려워진다.
    // 이를 쉽게 이해하기 위해서 상위 수준에서 모델을 조망할수 있는 방법이 필요한데 그것이 애그리거트 이다.
    // 애그리거트는 모델을 이해하는데 도움을 주면서 일관성을 관리하는 기준이기도 하다.
    // 한 애그리거트에 속한 객체는 다른 애그리거트에 속하지 않는다.
    // 이때 구분 기분이 되는것은 도메인 규칙과 요구사항 이다.
    // A가 B를 갖는다 라고 하더라도 하나의 애그리거트인것은 아니다. EX) 상품 , 리뷰
    // 대부분 하나의 애그리거트는 한개의 엔티티를 갖게 된다.
    // 애그리거트에 속한 모든 객체는 일관된 상태를 유지해야 하는데 이것을 관리하는것이 루트 엔티티 이며 애그리거트에 속한 객체는 루트 엔티티에 속하게 된다.


    // 애그리거트 간의 연관관계
    // 한 카테고리와 한 상품 사이의 관계는 1-N
    // 애그리거트에서 1-N 의 관계는 Set 으로 나타낼수 있다.

    public class Category{
        private Set<Product> products;

        public List<Product> getProducts(int page, int size) {
            List<Product> sortedProducts = sortById(Products);
            return sortedProducts.subList((page - 1) * size, page * size);
        }
    }

    // 위 코드는 Category 에 속한 모든 Product를 조회한다.
    // Product 수가 매우 많다면 이 코드를 실행할 때마다 실행 속도가 급격히 느려지게 된다.
    // 개념적으로 애그리거트 간에 1-N 연관이 있더라도 이런 성능 문제 때문에 애그리거트 간의 1-N 연관관계를 실제 구현에 반영하지 않는다.
    // 카테고리에 속한 상품을 구할 필요가 있다면 상품 입장에서 자신이 속한 카테고리를 N-1 로 연관지어 구하면 된다.
    // 이를 구현 모델에 반영하면 Product 에 다음과 같이 Category 로의 연관 관계를 추가하고 그 연관을 이용해서 특정 Category 에 속한 Product 를 구하면 된다.

    public class Product{
        private CategoryId categoryId;
    }

    // 카테고리에 속한 상품 목록을 제공하는 응용 서비스는 다음과 같이 categoryId 가 지정한 카테고리 식별자인 Product목록을 구한다.
    public class ProductListService{
        public Page<Product> getProductOfCagegory(Long categoryId, int page, int size) {
            Category category = categoryRepository.findById(categoryId);
            checkCategory(category);
            List<Product> products = productRepository.findByCategoryId(category.getId(), page, size);
            int totalCount = productRepository.countsByCategoryId(category.getId());
            return new Page(page, size, totalCount(CountedCompleter, products));
        }
    }

    // M-N 관계는 개념적으로 양쪽 애그리거트에 컬렉션으로 연관을 만든다.
    // 상품이 여러 카테고리에 속할수 있다고 가정하면 카테고리와 상품은 M-N 연관관계를 멪는다.
    // 보통 특정 카테고리에 속한 상품들을 보여줄 때 목록 화면에서 각 상품이 속한 모든 카테고리 정보는 상품 정보에 포함하지 않는다.

    public class Product{
        private Set<CategoryId> categoryIds;
    }

    // 카테고리와 상품 의 관계를 JPA 관점에서 보면 @ManyToMany 가 된다.
    // 중간 테이블을 생성해서 join 으로 처리하게 되겠지만 @ManyToMany 는 권장하는 방법이 아니라고 했다.
    // 이때 생성되는 중간 테이블을 직접 컨트롤 할수 없기 때문에 직접 중간 테이블을 만들어서 N:M -> 1:N , N:1 로 구현해야 한다.
    // 4장에서 다룬다고 하니 우선 진행하자.


    // 3.6 애그리거트를 팩토리로 사용하기.

    // 고객이 특정 상점을 여러차례 신고해서 해당 상점이 더 이상 물건을 등록하지 못하도록 차단한 상태라고 하자.
    // 상품 등록 기능을 구현한 응용 서비스는 다음과 같다.

    public class RegisterProductService{
        public ProductId registerNewProduct(NewProductRequest request) {
            Store store = storeRepository.findById(request.getStoreId());
            checkNull(store);
            if (store.isBlocked()) {
                throw new StoreBlockedException();
            }
            ProductId id = productRepository.nextId();
            Product product = new Product(id,store.getId(), ...);
            productRepository.save(product);
            return id;

        }
    }

    // 위의 코드를 보면 중요한 로직 처리가 응용 서비스에 노출되어 있다.
    // Store 가 Product 를 생성할수 있는지를 판단하고 Product 를 생성하는 것은 논리적으로 하나의 도메인 기능인데 이 도메인 기능을 응용 서비스에서 구현하고있다.
    // 이 기능을 Store 애그리거트로 옮겨보자.

    public class Store {
        public Product createProduct(ProductId newProductId, ...) {
            if (isBlocked()) {
                throw new StroeBlockedException();
            }

            return new Product(newProductId,getId(), ...);

        }
    }

    // Store 애그리거트의 createProduct() 는 Product 애그리거트를 생성하는 팩토리 역할을 하면서 중요한 도메인 로직을 구현하고 있다.

    public class RegisterProductService{
        public ProductId registerNewProduct(NewProductRequest request) {
            Store store = storeRepository.findById(request.getStoreId());
            checkNull(store);
//            if (store.isBlocked()) {
//                throw new StoreBlockedException();
//            }
            ProductId id = productRepository.nextId();
            // add
            Product product = store.createProduct();
            productRepository.save(product);
            return id;

        }
    }

    // 응용 서비스에서 더이상 Store 의 상태를 확인하지 않게 되었다.
    // Store 가 Product 를 생성할수 있는지 여부는 Store 에서 구현하였다.
    // 애그리거트가 갖고 있는 데이터를 이용해서 다른 대그리거트를 생성해야 한다면 이처럼 애그리거트에 팩토리 메서드를 구현하자.
}
