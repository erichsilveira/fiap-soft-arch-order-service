package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class OrderProduct {

    private final String id;

    private final String orderId;

    private final String productId;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
