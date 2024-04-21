package com.fiap.orders.domain.service;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.interfaces.dto.UpdateOrderCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private UpdateOrderUseCaseImpl updateOrderUseCase;

    private Order existingOrder;

    @BeforeEach
    void setUp() {
        existingOrder = new Order("order123", null, null, OrderStatus.REQUESTED, "customer123", null, null);
    }

    @Test
    void shouldUpdateOrderSuccessfully() throws ResourceNotFoundException {
        UpdateOrderCommand command = new UpdateOrderCommand("order123", OrderStatus.PREPARING);
        existingOrder.setStatus(OrderStatus.PREPARING);
        when(orderRepository.searchOrderById("order123")).thenReturn(existingOrder);
        when(orderRepository.updateOrder("order123", OrderStatus.PREPARING)).thenReturn(existingOrder);

        Order updatedOrder = updateOrderUseCase.updateOrder(command);

        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.PREPARING, updatedOrder.getStatus());
        verify(orderRepository).updateOrder("order123", OrderStatus.PREPARING);
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatingFromWaitingPayment() {
        existingOrder.setStatus(OrderStatus.WAITING_PAYMENT);
        UpdateOrderCommand command = new UpdateOrderCommand("order123", OrderStatus.PREPARING);
        when(orderRepository.searchOrderById("order123")).thenReturn(existingOrder);

        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            updateOrderUseCase.updateOrder(command);
        });

        assertEquals("Cannot update order status from WAITING_PAYMENT", thrown.getMessage());
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatingToWaitingPayment() {
        UpdateOrderCommand command = new UpdateOrderCommand("order123", OrderStatus.WAITING_PAYMENT);
        when(orderRepository.searchOrderById("order123")).thenReturn(existingOrder);

        ConflictException thrown = assertThrows(ConflictException.class, () -> {
            updateOrderUseCase.updateOrder(command);
        });

        assertEquals("Cannot update order status to WAITING_PAYMENT", thrown.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionIfOrderNotFound() {
        UpdateOrderCommand command = new UpdateOrderCommand("nonexistentOrder", OrderStatus.PREPARING);
        when(orderRepository.searchOrderById("nonexistentOrder")).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> {
            updateOrderUseCase.updateOrder(command);
        });
    }
}
