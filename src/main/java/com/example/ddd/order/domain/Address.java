package com.example.ddd.order.domain;

public class Address {
    private String address1;
    private String address2;
    private String zipCode;

    public Address(String address1, String address2, String zipCode) {
        this.address1 = address1;
        this.address2 = address2;
        this.zipCode = zipCode;
    }
}
