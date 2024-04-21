package com.fiap.orders.interfaces.dto;


import com.fiap.orders.domain.entity.OrderStatus;

public record SearchOrderCommand(String cpf, OrderStatus status) {

}
