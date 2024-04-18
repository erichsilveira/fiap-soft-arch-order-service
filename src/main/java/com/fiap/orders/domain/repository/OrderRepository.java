package com.fiap.orders.domain.repository;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.entity.OrderStatus;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
import java.util.List;

public interface OrderRepository {

    List<Order> searchOrders(String cpf, OrderStatus status);

    Order createOrder(Order domainEntity);

    Order updateOrder(String entityId, OrderStatus status) throws ResourceNotFoundException;

    Order searchOrderById(String id) throws ResourceNotFoundException;
}
