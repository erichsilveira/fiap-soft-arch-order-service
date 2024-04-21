package com.fiap.orders.interfaces.mapper;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.interfaces.dto.OrderCreationRequest;
import org.springframework.stereotype.Component;

@Component
public class OrderRestMapper {

    public Order toDomainEntity(OrderCreationRequest restEntity) {
        return Order.builder()
                .customerCpf(restEntity.customerCpf())
                .orderPrice(restEntity.orderPrice())
                .productsId(restEntity.productsId())
                .build();
    }
}
