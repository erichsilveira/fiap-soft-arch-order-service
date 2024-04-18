package com.fiap.orders.domain.service;

import com.fiap.techchallenge.application.usecases.CreateOrderCheckoutUseCase;
import com.fiap.techchallenge.application.usecases.CreatePaymentUseCase;
import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CreateOrderCheckoutUseCaseImpl implements CreateOrderCheckoutUseCase {

    private final OrderRepository orderRepository;

    private final CreatePaymentUseCase paymentUseCase;

    @Override
    public Order createOrderCheckout(Order order) {
        log.info("Creating order for customer cpf {} orderId {}",
            order.getCustomerCpf(), order.getId());

        Order createdOrder = orderRepository.createOrder(order);

        paymentUseCase.createPayment(createdOrder.getId(), createdOrder.getOrderPrice());

        return createdOrder;
    }
}
