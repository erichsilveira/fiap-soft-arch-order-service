package com.fiap.orders.application.usecases;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.interfaces.dto.UpdateOrderCommand;
import com.fiap.orders.exception.ResourceNotFoundException;

public interface UpdateOrderUseCase {

    Order updateOrder(UpdateOrderCommand command) throws ResourceNotFoundException;
}
