package com.example.ddd.order.domain;

import org.springframework.data.history.Revision;

public class ShippingInfo {
    // 받는사람
//    private String receiverName;
//    private String receiverPhoneNumber;
    private Receiver receiver;

    // 주소
//    private String shippingAddress1;
//    private String shippingAddress2;
//    private String shippingZipcode;
    private Address address;


    public ShippingInfo(String receiverName, String receiverPhoneNumber, String shippingAddress1,
                        String shippingAddress2,
                        String shippingZipcode) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.shippingAddress1 = shippingAddress1;
        this.shippingAddress2 = shippingAddress2;
        this.shippingZipcode = shippingZipcode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public String getShippingZipcode() {
        return shippingZipcode;
    }
}
