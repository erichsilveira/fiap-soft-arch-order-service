package com.fiap.orders.domain.repository;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.exception.ResourceNotFoundException;
import java.util.List;

public interface OrderRepository {

    List<Order> searchOrders(String cpf, OrderStatus status);

    Order createOrder(Order domainEntity);

    Order updateOrder(String entityId, OrderStatus status) throws ResourceNotFoundException;

    Order searchOrderById(String id) throws ResourceNotFoundException;
}
