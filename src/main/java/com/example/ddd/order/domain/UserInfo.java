package com.example.ddd.order.domain;

public class UserInfo {
    private String id;
    private String name;

    public UserInfo() {
    }

    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    // setter 는 도메인의 핵심 개념이나 의도를 코드에서 사라지게 하므로 무조건적인 추가는 좋지 않다.
}
