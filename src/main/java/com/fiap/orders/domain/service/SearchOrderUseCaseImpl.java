package com.fiap.orders.domain.service;

import com.fiap.techchallenge.application.usecases.SearchOrderUseCase;
import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.repository.OrderRepository;
import com.fiap.techchallenge.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.techchallenge.interfaces.dto.SearchOrderCommand;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
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
