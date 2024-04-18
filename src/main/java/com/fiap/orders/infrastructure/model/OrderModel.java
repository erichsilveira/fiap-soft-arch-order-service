package com.fiap.orders.infrastructure.model;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.entity.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {


    public static OrderModel toOrderModel(Order order) {
        return OrderModel.builder()
                .id(order.getId())
                .products(order.getProductsId().stream().map(productId ->
                        OrderProductModel.builder().product(ProductModel.builder().id(productId).build())
                                .order(OrderModel.builder().id(order.getId()).build()).build()).toList())
                .orderPrice(order.getOrderPrice())
                .status(order.getStatus())
                .customer(CustomerModel.builder().id(order.getCustomerId()).build())
                .customerCpf(order.getCustomerCpf())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static Order toOrder(OrderModel orderModel) {
        return Order.builder()
                .id(orderModel.getId())
                .productsId(orderModel.getProducts().stream().map(orderProductModel -> orderProductModel.getProduct().getId()).toList())
                .orderPrice(orderModel.getOrderPrice())
                .status(orderModel.getStatus())
                .customerId(orderModel.getCustomer().getId())
                .customerCpf(orderModel.getCustomerCpf())
                .createdAt(orderModel.getCreatedAt())
                .updatedAt(orderModel.getUpdatedAt())
                .build();
    }

    @Id
    @GeneratedValue
    @UuidGenerator
    private String id;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderProductModel> products;

    private BigDecimal orderPrice;

    private OrderStatus status;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private CustomerModel customer;

    private String customerCpf;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
