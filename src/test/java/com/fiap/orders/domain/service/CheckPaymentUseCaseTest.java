package com.fiap.orders.domain.service;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.infrastructure.client.PaymentsClient;
import com.fiap.orders.interfaces.dto.PaymentsStatusResponse;
import com.fiap.orders.mocks.OrderMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckPaymentUseCaseTest {

    @Mock
    private PaymentsClient paymentsClient;

    @Mock
    private OrderRepository ordersRepository;

    @InjectMocks
    private CheckPaymentUseCaseImpl checkPaymentUseCase;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        // Setup a sample order using OrderMock for testing
        sampleOrder = OrderMock.generateOrder("order123", new BigDecimal("100.00"), OrderStatus.WAITING_PAYMENT, "customer123");
    }

    @Test
    void shouldUpdateOrderStatusWhenPaymentApproved() {
        // Arrange
        when(ordersRepository.searchOrders(null, OrderStatus.WAITING_PAYMENT))
                .thenReturn(Collections.singletonList(sampleOrder));

        PaymentsStatusResponse paymentResponse = new PaymentsStatusResponse(
                "payment123", "order123", new BigDecimal("100.00"), "APPROVED", LocalDateTime.now(), LocalDateTime.now());
        when(paymentsClient.getPaymentsByOrder("order123")).thenReturn(Collections.singletonList(paymentResponse));

        // Act
        checkPaymentUseCase.scheduledPaymentUpdate();

        // Assert
        verify(ordersRepository).updateOrder("order123", OrderStatus.REQUESTED);
    }

    @Test
    void shouldNotUpdateOrderWhenPaymentNotApproved() {
        // Arrange
        when(ordersRepository.searchOrders(null, OrderStatus.WAITING_PAYMENT))
                .thenReturn(Collections.singletonList(sampleOrder));

        PaymentsStatusResponse paymentResponse = new PaymentsStatusResponse(
                "payment123", "order123", new BigDecimal("100.00"), "PENDING", LocalDateTime.now(), LocalDateTime.now());
        when(paymentsClient.getPaymentsByOrder("order123")).thenReturn(Collections.singletonList(paymentResponse));

        // Act
        checkPaymentUseCase.scheduledPaymentUpdate();

        // Assert
        verify(ordersRepository, never()).updateOrder(anyString(), any(OrderStatus.class));
    }

    @Test
    void shouldHandleNoPaymentsFound() {
        // Arrange
        when(ordersRepository.searchOrders(null, OrderStatus.WAITING_PAYMENT))
                .thenReturn(Collections.singletonList(sampleOrder));

        when(paymentsClient.getPaymentsByOrder("order123")).thenReturn(Collections.emptyList());

        // Act
        checkPaymentUseCase.scheduledPaymentUpdate();

        // Assert
        verify(ordersRepository, never()).updateOrder(anyString(), any(OrderStatus.class));
    }
}
