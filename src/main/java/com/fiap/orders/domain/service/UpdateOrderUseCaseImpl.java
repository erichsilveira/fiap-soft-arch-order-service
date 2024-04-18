package com.fiap.orders.domain.service;

import com.fiap.techchallenge.application.usecases.UpdateOrderUseCase;
import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.repository.OrderRepository;
import com.fiap.techchallenge.interfaces.dto.UpdateOrderCommand;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
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
        return repository.updateOrder(command.entityId(), command.status());
    }
}
