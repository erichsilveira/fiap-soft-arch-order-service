package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.CheckProductsUseCase;
import com.fiap.orders.application.usecases.CreateOrderUseCase;
import com.fiap.orders.application.usecases.ExistsCustomerUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.exception.ResourceNotFoundException;
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
    private final CheckProductsUseCase checkProductsUseCase;

    @Override
    public Order createOrder(Order order) {
        log.info("Creating order for customer cpf {} orderId {}",
                order.getCustomerCpf(), order.getId());

        if (Objects.nonNull(order.getCustomerCpf())) {
            if (!customerUseCase.existsCustomer(order.getCustomerCpf())) {
                throw new ConflictException("User not registered");
            }
        }

        try {
            checkProductsUseCase.checkProducts(order.getProductsId());
        } catch (ResourceNotFoundException ex) {
            log.info("Some of the products are not registered");
            throw new ConflictException("Some of the products are not registered");
        }

        return orderRepository.createOrder(order);
    }
}
