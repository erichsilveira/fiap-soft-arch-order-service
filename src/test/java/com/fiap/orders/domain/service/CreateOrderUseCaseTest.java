package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.CheckProductsUseCase;
import com.fiap.orders.application.usecases.ExistsCustomerUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ExistsCustomerUseCase existsCustomerUseCase;
    @Mock
    private CheckProductsUseCase checkProductsUseCase;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    @Test
    void shouldNotThrowConflictExceptionIfCustomerIsNull() {
        Order sampleOrder = new Order("order123", Arrays.asList("prod123", "prod456"), null, null, null, null, null);

        assertDoesNotThrow(() -> createOrderUseCase.createOrder(sampleOrder));
        verify(existsCustomerUseCase, never()).existsCustomer(any());
        verify(checkProductsUseCase, times(1)).checkProducts(any());
        verify(orderRepository, times(1)).createOrder(any());
    }

    @Test
    void shouldThrowConflictExceptionIfCustomerDoesNotExist() {
        Order sampleOrder = new Order("order123", Arrays.asList("prod123", "prod456"), null, null, "1234567890", null, null);
        when(existsCustomerUseCase.existsCustomer("1234567890")).thenReturn(false);

        assertThrows(ConflictException.class, () -> createOrderUseCase.createOrder(sampleOrder));
        verify(existsCustomerUseCase).existsCustomer("1234567890");
        verify(checkProductsUseCase, never()).checkProducts(any());
        verify(orderRepository, never()).createOrder(any());
    }

    @Test
    void shouldThrowConflictExceptionIfAnyProductDoesNotExist() {
        Order sampleOrder = new Order("order123", Arrays.asList("prod123", "prod456"), null, null, "1234567890", null, null);
        when(existsCustomerUseCase.existsCustomer("1234567890")).thenReturn(true);
        doThrow(ResourceNotFoundException.class).when(checkProductsUseCase).checkProducts(Arrays.asList("prod123", "prod456"));

        assertThrows(ConflictException.class, () -> createOrderUseCase.createOrder(sampleOrder));
        verify(existsCustomerUseCase).existsCustomer("1234567890");
        verify(checkProductsUseCase).checkProducts(Arrays.asList("prod123", "prod456"));
        verify(orderRepository, never()).createOrder(any());
    }

    @Test
    void shouldCreateOrderWhenCustomerAndProductsExist() {
        Order sampleOrder = new Order("order123", Arrays.asList("prod123", "prod456"), null, null, "1234567890", null, null);
        when(existsCustomerUseCase.existsCustomer("1234567890")).thenReturn(true);
        doNothing().when(checkProductsUseCase).checkProducts(Arrays.asList("prod123", "prod456"));
        when(orderRepository.createOrder(any(Order.class))).thenReturn(sampleOrder);

        Order createdOrder = createOrderUseCase.createOrder(sampleOrder);

        assertNotNull(createdOrder);
        verify(existsCustomerUseCase).existsCustomer("1234567890");
        verify(checkProductsUseCase).checkProducts(Arrays.asList("prod123", "prod456"));
        verify(orderRepository).createOrder(sampleOrder);
    }
}
