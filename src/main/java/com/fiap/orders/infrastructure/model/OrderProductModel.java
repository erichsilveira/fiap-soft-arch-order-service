package com.fiap.orders.infrastructure.model;

import com.fiap.techchallenge.domain.entity.OrderProduct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.ZonedDateTime;

@Entity
@Table(name = "order_products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductModel {


    public static OrderProductModel toOrderProductModel(OrderProduct orderProduct) {
        return OrderProductModel.builder()
                .id(orderProduct.getId())
                .order(OrderModel.builder().id(orderProduct.getOrderId()).build())
                .product(ProductModel.builder().id(orderProduct.getProductId()).build())
                .createdAt(orderProduct.getCreatedAt())
                .updatedAt(orderProduct.getUpdatedAt())
                .build();
    }

    public static OrderProduct toOrderProduct(OrderProductModel orderProductModel) {
        return OrderProduct.builder()
                .id(orderProductModel.getId())
                .orderId(orderProductModel.getOrder().getId())
                .productId(orderProductModel.getProduct().getId())
                .createdAt(orderProductModel.getCreatedAt())
                .updatedAt(orderProductModel.getUpdatedAt())
                .build();
    }

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private OrderModel order;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private ProductModel product;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
