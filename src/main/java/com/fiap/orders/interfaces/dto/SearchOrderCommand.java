package com.fiap.orders.interfaces.dto;


import com.fiap.techchallenge.domain.entity.OrderStatus;

public record SearchOrderCommand(String cpf, OrderStatus status) {

}
