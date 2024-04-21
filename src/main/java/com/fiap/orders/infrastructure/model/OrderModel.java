package com.fiap.orders.infrastructure.model;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("orders")
public class OrderModel {

    public static OrderModel toOrderModel(Order order) {
        return OrderModel.builder()
                .id(order.getId())
                .productsIds(order.getProductsId())
                .orderPrice(order.getOrderPrice())
                .status(order.getStatus())
                .customerCpf(order.getCustomerCpf())
                .customerCpf(order.getCustomerCpf())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static Order toOrder(OrderModel orderModel) {
        return Order.builder()
                .id(orderModel.getId())
                .productsId(orderModel.productsIds)
                .orderPrice(orderModel.getOrderPrice())
                .status(orderModel.getStatus())
                .customerCpf(orderModel.getCustomerCpf())
                .createdAt(orderModel.getCreatedAt())
                .updatedAt(orderModel.getUpdatedAt())
                .build();
    }

    @Id
    private String id;

    private List<String> productsIds;

    private BigDecimal orderPrice;

    private OrderStatus status;

    private String customerCpf;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
