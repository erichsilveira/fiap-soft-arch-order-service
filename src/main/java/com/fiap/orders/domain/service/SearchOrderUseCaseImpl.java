package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.SearchOrderUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.orders.interfaces.dto.SearchOrderCommand;
import com.fiap.orders.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SearchOrderUseCaseImpl implements SearchOrderUseCase {

    private final OrderRepository repository;

    @Override
    public List<Order> searchOrders(SearchOrderCommand command) {
        return repository.searchOrders(command.cpf(), command.status());
    }

    @Override
    public Order searchOrderById(SearchOrderByIdCommand command) throws ResourceNotFoundException {
        return repository.searchOrderById(command.id());
    }
}
