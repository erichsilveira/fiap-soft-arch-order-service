package com.fiap.orders.application.usecases;

import com.fiap.orders.domain.entity.Order;

public interface CreateOrderUseCase {

    Order createOrder(Order command);
}
