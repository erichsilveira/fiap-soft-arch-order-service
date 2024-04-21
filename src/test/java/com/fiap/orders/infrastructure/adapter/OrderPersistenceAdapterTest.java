package com.fiap.orders.infrastructure.adapter;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.infrastructure.model.OrderModel;
import com.fiap.orders.infrastructure.persistence.OrderMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderPersistenceAdapterTest {

    @Mock
    private OrderMongoRepository orderMongoRepository;

    @InjectMocks
    private OrderPersistenceAdapter orderPersistenceAdapter;

    private Order order;
    private OrderModel orderModel;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        order = Order.builder()
                .id("1")
                .productsId(List.of("prod1", "prod2"))
                .orderPrice(new BigDecimal("100.00"))
                .status(OrderStatus.REQUESTED)
                .customerCpf("1234567890")
                .createdAt(now)
                .updatedAt(now)
                .build();
        orderModel = OrderModel.toOrderModel(order);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        when(orderMongoRepository.save(any(OrderModel.class))).thenReturn(orderModel);
        Order createdOrder = orderPersistenceAdapter.createOrder(order);
        assertNotNull(createdOrder);
        assertEquals(order.getId(), createdOrder.getId());
    }

    @Test
    void shouldFindOrderByIdSuccessfully() throws ResourceNotFoundException {
        when(orderMongoRepository.findById("1")).thenReturn(Optional.of(orderModel));
        Order foundOrder = orderPersistenceAdapter.searchOrderById("1");
        assertNotNull(foundOrder);
        assertEquals("1", foundOrder.getId());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFound() {
        when(orderMongoRepository.findById("2")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderPersistenceAdapter.searchOrderById("2"));
    }

    @Test
    void shouldUpdateOrderStatusSuccessfully() throws ResourceNotFoundException {
        when(orderMongoRepository.findById("1")).thenReturn(Optional.of(orderModel));
        when(orderMongoRepository.save(orderModel)).thenReturn(orderModel);
        Order updatedOrder = orderPersistenceAdapter.updateOrder("1", OrderStatus.DELIVERED);
        assertNotNull(updatedOrder);
        assertEquals(OrderStatus.DELIVERED, updatedOrder.getStatus());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFoundForUpdate() {
        // Configurando o comportamento do mock para retornar um Optional vazio
        when(orderMongoRepository.findById("2")).thenReturn(Optional.empty());

        // Verificando se uma exceção ResourceNotFoundException é lançada
        assertThrows(ResourceNotFoundException.class, () -> {
            // Chamando o método que esperamos que lance a exceção
            orderPersistenceAdapter.updateOrder("2", OrderStatus.DELIVERED);
        });
    }

    @Test
    void shouldSearchOrdersWithFilters() {
        when(orderMongoRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(List.of(orderModel));
        List<Order> foundOrders = orderPersistenceAdapter.searchOrders("1234567890", OrderStatus.REQUESTED);
        assertFalse(foundOrders.isEmpty());
        assertEquals(1, foundOrders.size());
        assertEquals("1234567890", foundOrders.get(0).getCustomerCpf());
    }

    @Test
    void shouldSearchOrdersByCpf() {
        // Configurar dados de exemplo
        List<OrderModel> orderModels = Arrays.asList(
                new OrderModel("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "1234567890", LocalDateTime.now(), null),
                new OrderModel("2", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "1234567890", LocalDateTime.now(), null)
        );
        when(orderMongoRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(orderModels);

        // Chamada ao método de busca
        List<Order> foundOrders = orderPersistenceAdapter.searchOrders("1234567890", null);

        // Verificar se os pedidos foram encontrados corretamente
        assertFalse(foundOrders.isEmpty());
        assertEquals(2, foundOrders.size());
        assertEquals("1234567890", foundOrders.get(0).getCustomerCpf());
        assertEquals("1234567890", foundOrders.get(1).getCustomerCpf());
    }

    @Test
    void shouldSearchOrdersByStatus() {
        // Configurar dados de exemplo
        List<OrderModel> orderModels = Arrays.asList(
                new OrderModel("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "1234567890", LocalDateTime.now(), null),
                new OrderModel("2", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "0987654321", LocalDateTime.now(), null)
        );
        when(orderMongoRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(orderModels);

        // Chamada ao método de busca
        List<Order> foundOrders = orderPersistenceAdapter.searchOrders(null, OrderStatus.REQUESTED);

        // Verificar se os pedidos foram encontrados corretamente
        assertFalse(foundOrders.isEmpty());
        assertEquals(2, foundOrders.size());
        assertEquals(OrderStatus.REQUESTED, foundOrders.get(0).getStatus());
        assertEquals(OrderStatus.REQUESTED, foundOrders.get(1).getStatus());
    }

    @Test
    void shouldSearchOrdersByCpfAndStatus() {
        // Configurar dados de exemplo
        List<OrderModel> orderModels = Arrays.asList(
                new OrderModel("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "1234567890", LocalDateTime.now(), null)
        );
        when(orderMongoRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(orderModels);

        // Chamada ao método de busca
        List<Order> foundOrders = orderPersistenceAdapter.searchOrders("1234567890", OrderStatus.REQUESTED);

        // Verificar se o pedido foi encontrado corretamente
        assertFalse(foundOrders.isEmpty());
        assertEquals(1, foundOrders.size());
        assertEquals("1234567890", foundOrders.get(0).getCustomerCpf());
        assertEquals(OrderStatus.REQUESTED, foundOrders.get(0).getStatus());
    }

    @Test
    void shouldSearchAllOrders() {
        // Configurar dados de exemplo
        List<OrderModel> orderModels = Arrays.asList(
                new OrderModel("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "1234567890", LocalDateTime.now(), null),
                new OrderModel("2", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.DELIVERED, "0987654321", LocalDateTime.now(), null)
        );
        when(orderMongoRepository.findAll(any(Example.class), any(Sort.class))).thenReturn(orderModels);

        // Chamada ao método de busca
        List<Order> foundOrders = orderPersistenceAdapter.searchOrders(null, null);

        // Verificar se os pedidos foram encontrados corretamente
        assertFalse(foundOrders.isEmpty());
        assertEquals(2, foundOrders.size());
        assertEquals("1234567890", foundOrders.get(0).getCustomerCpf());
        assertEquals("0987654321", foundOrders.get(1).getCustomerCpf());
        assertEquals(OrderStatus.REQUESTED, foundOrders.get(0).getStatus());
        assertEquals(OrderStatus.DELIVERED, foundOrders.get(1).getStatus());
    }
}

