package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class Order {

    private final String id;

    private final List<String> productsId;

    private final BigDecimal orderPrice;

    private OrderStatus status;

    private final String customerCpf;

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
