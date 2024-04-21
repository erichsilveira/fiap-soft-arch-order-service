package com.fiap.orders.application.usecases;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.orders.interfaces.dto.SearchOrderCommand;
import com.fiap.orders.exception.ResourceNotFoundException;
import java.util.List;

public interface SearchOrderUseCase {

    List<Order> searchOrders(SearchOrderCommand command);

    Order searchOrderById(SearchOrderByIdCommand command) throws ResourceNotFoundException;
}
