package com.example.ddd.order.domain;

public interface CancelPolicy {
    boolean hasCancellationPermission(Order order, Canceller canceller);
}
