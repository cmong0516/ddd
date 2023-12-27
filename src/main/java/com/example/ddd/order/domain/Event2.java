package com.example.ddd.order.domain;

public abstract class Event2 {
    private long timestamp;

    public Event2(){
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
