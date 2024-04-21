package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.CheckPaymentUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.infrastructure.client.PaymentsClient;
import com.fiap.orders.interfaces.dto.PaymentsStatusResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CheckPaymentUseCaseImpl implements CheckPaymentUseCase {

    private PaymentsClient paymentsClient;
    private OrderRepository ordersRepository;

    @Override
    @Scheduled(fixedRate = 60000)
    public void scheduledPaymentUpdate() {

        log.info("Checking payment status");

        List<Order> orders = ordersRepository.searchOrders(null, OrderStatus.WAITING_PAYMENT);

        log.info("Found {} orders to check", orders.size());

        orders.forEach(order -> {
            log.info("Checking payment status for order {}", order.getId());

            List<PaymentsStatusResponse> paymentStatus = paymentsClient.getPaymentsByOrder(order.getId());

            if (Objects.nonNull(paymentStatus) && !paymentStatus.isEmpty()) {
                log.info("Payment status for order {}: {}", order.getId(), paymentStatus.get(0).status());

                if (paymentStatus.get(0).status().equals("APPROVED")) {
                    ordersRepository.updateOrder(order.getId(), OrderStatus.REQUESTED);
                }
            } else {
                log.error("Payment not found for order {}", order.getId());
            }
        });
    }
}
