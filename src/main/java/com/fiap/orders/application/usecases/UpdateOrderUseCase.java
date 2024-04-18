package com.fiap.orders.application.usecases;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.interfaces.dto.UpdateOrderCommand;
import com.fiap.techchallenge.exception.ResourceNotFoundException;

public interface UpdateOrderUseCase {

    Order updateOrder(UpdateOrderCommand command) throws ResourceNotFoundException;
}
