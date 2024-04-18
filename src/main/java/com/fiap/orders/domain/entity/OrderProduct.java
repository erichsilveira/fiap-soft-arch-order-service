package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Builder
public class OrderProduct {

    @Getter
    private final String id;

    @Getter
    private final String orderId;

    @Getter
    private final String productId;

    @Getter
    private ZonedDateTime createdAt;

    @Getter
    private ZonedDateTime updatedAt;
}
