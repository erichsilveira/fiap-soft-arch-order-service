package com.fiap.orders.interfaces.mapper;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.interfaces.dto.OrderCreationRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderRestMapper {

    public Order toDomainEntity(OrderCreationRequest restEntity) {
        return Order.builder()
            .customerCpf(restEntity.customerCpf())
            .customerId(restEntity.customerId())
            .orderPrice(restEntity.orderPrice())
            .productsId(restEntity.productsId())
            .build();
    }
}
