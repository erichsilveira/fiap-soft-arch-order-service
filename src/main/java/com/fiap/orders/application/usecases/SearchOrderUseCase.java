package com.fiap.orders.application.usecases;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.techchallenge.interfaces.dto.SearchOrderCommand;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
import java.util.List;

public interface SearchOrderUseCase {

    List<Order> searchOrders(SearchOrderCommand command);

    Order searchOrderById(SearchOrderByIdCommand command) throws ResourceNotFoundException;
}
