package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
public class Order {

    @Getter
    private final String id;

    @Getter
    private final List<String> productsId;

    @Getter
    private BigDecimal orderPrice;

    @Getter
    private final OrderStatus status;

    @Getter
    private final String customerId;

    @Getter
    private final String customerCpf;

    @Getter
    private ZonedDateTime createdAt;

    @Getter
    private ZonedDateTime updatedAt;
}
