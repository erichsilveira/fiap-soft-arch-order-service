package com.fiap.orders.infrastructure.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.application.usecases.CreateOrderUseCase;
import com.fiap.orders.application.usecases.SearchOrderUseCase;
import com.fiap.orders.application.usecases.UpdateOrderUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.infrastructure.persistence.CustomerMongoRepository;
import com.fiap.orders.infrastructure.persistence.OrderMongoRepository;
import com.fiap.orders.interfaces.dto.OrderCreationRequest;
import com.fiap.orders.interfaces.dto.OrderUpdateRequest;
import com.fiap.orders.interfaces.mapper.OrderRestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@Import(OrderRestMapper.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UpdateOrderUseCase updateOrderUseCase;

    @MockBean
    private SearchOrderUseCase searchOrderUseCase;

    @MockBean
    private CreateOrderUseCase createOrderUseCase;

    @Autowired
    private OrderRestMapper orderRestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomerMongoRepository customerMongoRepository;

    @MockBean
    private OrderMongoRepository orderMongoRepository;

    @Test
    void search() throws Exception {
        List<Order> expectedOrders = Collections.singletonList(
                new Order("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "12345678900", LocalDateTime.now(), null));
        given(searchOrderUseCase.searchOrders(ArgumentMatchers.any())).willReturn(expectedOrders);

        mockMvc.perform(get("/orders")
                        .param("cpf", "12345678900")
                        .param("status", "REQUESTED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].status").value("REQUESTED"));
    }

    @Test
    void searchById() throws Exception {
        Order expectedOrder = new Order("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "12345678900", LocalDateTime.now(), null);
        given(searchOrderUseCase.searchOrderById(any())).willReturn(expectedOrder);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }

    @Test
    void updateShouldReturnConflictWhenConflictExceptionThrown() throws Exception {
        String orderId = "1";
        OrderUpdateRequest updateRequest = new OrderUpdateRequest(OrderStatus.REQUESTED);
        doThrow(new ConflictException("Order conflict")).when(updateOrderUseCase).updateOrder(any());

        // Executa a requisição PUT para o endpoint /orders/{id}
        mockMvc.perform(put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isConflict()) // Verifica se o status da resposta é 409 Conflict
                .andExpect(content().string("Order conflict")); // Verifica se a mensagem de erro é retornada corretamente

        // Verifica se o método updateOrderUseCase.updateOrder foi chamado exatamente uma vez com qualquer argumento
        verify(updateOrderUseCase, times(1)).updateOrder(any());
    }

    @Test
    void createShouldReturnConflictWhenConflictExceptionThrown() throws Exception {
        OrderCreationRequest creationRequest = new OrderCreationRequest(Collections.singletonList("product1"), "12345678900", BigDecimal.TEN);
        given(createOrderUseCase.createOrder(any())).willThrow(new ConflictException("Order creation conflict"));

        // Executa a requisição POST para o endpoint /orders
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isConflict()) // Verifica se o status da resposta é 409 Conflict
                .andExpect(content().string("Order creation conflict")); // Verifica se a mensagem de erro é retornada corretamente

        // Verifica se o método createOrderUseCase.createOrder foi chamado exatamente uma vez com qualquer argumento
        verify(createOrderUseCase, times(1)).createOrder(any());
    }

    @Test
    void update() throws Exception {
        Order updatedOrder = new Order("1", Collections.emptyList(), BigDecimal.ZERO, OrderStatus.REQUESTED, "12345678900", LocalDateTime.now(), null);
        given(updateOrderUseCase.updateOrder(ArgumentMatchers.any())).willReturn(updatedOrder);

        OrderUpdateRequest updateRequest = new OrderUpdateRequest(OrderStatus.REQUESTED);

        mockMvc.perform(put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }

    @Test
    void create() throws Exception {
        OrderCreationRequest creationRequest = new OrderCreationRequest(Collections.singletonList("product1"), "12345678900", BigDecimal.TEN);
        Order expectedOrder = new Order("1", Collections.singletonList("product1"), BigDecimal.TEN, OrderStatus.REQUESTED, "12345678900", LocalDateTime.now(), null);
        given(createOrderUseCase.createOrder(ArgumentMatchers.any())).willReturn(expectedOrder);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(creationRequest)))
                .andExpect(status().isCreated());
    }
}
