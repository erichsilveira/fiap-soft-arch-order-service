package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.UpdateOrderUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.interfaces.dto.UpdateOrderCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderRepository repository;

    @Override
    public Order updateOrder(UpdateOrderCommand command) throws ResourceNotFoundException {
        log.info("Updating order {} to status {}", command.entityId(), command.status());

        Order currentOrder = repository.searchOrderById(command.entityId());

        if (currentOrder.getStatus().equals(OrderStatus.WAITING_PAYMENT)) {
            log.error("Cannot update order status from WAITING_PAYMENT");
            throw new ConflictException("Cannot update order status from WAITING_PAYMENT");
        }

        if (command.status().equals(OrderStatus.WAITING_PAYMENT)) {
            log.error("Cannot update order status to WAITING_PAYMENT");
            throw new ConflictException("Cannot update order status to WAITING_PAYMENT");
        }

        return repository.updateOrder(command.entityId(), command.status());
    }
}
