package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.CreateOrderUseCase;
import com.fiap.orders.application.usecases.ExistsCustomerUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
@Slf4j
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ExistsCustomerUseCase customerUseCase;

    @Override
    public Order createOrder(Order order) {
        log.info("Creating order for customer cpf {} orderId {}",
                order.getCustomerCpf(), order.getId());

        if (Objects.nonNull(order.getCustomerCpf())) {
            if (!customerUseCase.existsCustomer(order.getCustomerCpf())) {
                throw new ConflictException("User not registered");
            }
        }

        return orderRepository.createOrder(order);
    }
}
