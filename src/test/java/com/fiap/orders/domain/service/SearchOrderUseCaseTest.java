package com.fiap.orders.domain.service;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.orders.interfaces.dto.SearchOrderCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchOrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private SearchOrderUseCaseImpl searchOrderUseCase;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        // Setup a sample order with realistic data
        sampleOrder = Order.builder()
                .id("order123")
                .productsId(Arrays.asList("prod123", "prod456"))
                .orderPrice(new BigDecimal("100.00"))
                .status(OrderStatus.WAITING_PAYMENT)
                .customerCpf("12345678901")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldReturnListOfOrdersBasedOnCriteria() {
        SearchOrderCommand command = new SearchOrderCommand("12345678901", OrderStatus.WAITING_PAYMENT);
        when(orderRepository.searchOrders(command.cpf(), command.status())).thenReturn(Arrays.asList(sampleOrder));

        List<Order> results = searchOrderUseCase.searchOrders(command);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("order123", results.get(0).getId());
        verify(orderRepository).searchOrders(command.cpf(), command.status());
    }

    @Test
    void shouldReturnOrderWhenIdIsFound() throws ResourceNotFoundException {
        SearchOrderByIdCommand command = new SearchOrderByIdCommand("order123");
        when(orderRepository.searchOrderById(command.id())).thenReturn(sampleOrder);

        Order result = searchOrderUseCase.searchOrderById(command);

        assertNotNull(result);
        assertEquals("order123", result.getId());
        verify(orderRepository).searchOrderById(command.id());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenIdIsNotFound() {
        SearchOrderByIdCommand command = new SearchOrderByIdCommand("nonExistentOrder");
        when(orderRepository.searchOrderById(command.id())).thenThrow(new ResourceNotFoundException());

        assertThrows(ResourceNotFoundException.class, () -> {
            searchOrderUseCase.searchOrderById(command);
        });

        verify(orderRepository).searchOrderById(command.id());
    }
}
