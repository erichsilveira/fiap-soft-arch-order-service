package com.fiap.orders.mocks;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.infrastructure.model.OrderModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderMock {

    public static OrderModel generateOrderModel(String id, BigDecimal price, OrderStatus status, String customerId) {
        return OrderModel.builder()
                .id(id)
                .productsIds(generateProductIds()) // Simulated product IDs
                .orderPrice(price)
                .status(status)
                .customerCpf(customerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Order generateOrder(String id, BigDecimal price, OrderStatus status, String customerId) {
        return convertToOrder(generateOrderModel(id, price, status, customerId));
    }

    // Utility method to convert OrderModel to Order
    private static Order convertToOrder(OrderModel orderModel) {
        return Order.builder()
                .id(orderModel.getId())
                .productsId(orderModel.getProductsIds())
                .orderPrice(orderModel.getOrderPrice())
                .status(orderModel.getStatus())
                .customerCpf(orderModel.getCustomerCpf())
                .createdAt(orderModel.getCreatedAt())
                .updatedAt(orderModel.getUpdatedAt())
                .build();
    }

    // Helper method to generate a list of product IDs
    private static List<String> generateProductIds() {
        return Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }
}
