package com.fiap.orders.infrastructure.web;

import com.fiap.techchallenge.application.usecases.CreateOrderCheckoutUseCase;
import com.fiap.techchallenge.application.usecases.SearchOrderUseCase;
import com.fiap.techchallenge.application.usecases.SearchPaymentUseCase;
import com.fiap.techchallenge.application.usecases.UpdateOrderUseCase;
import com.fiap.techchallenge.application.usecases.UpdatePaymentUseCase;
import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.entity.OrderStatus;
import com.fiap.techchallenge.domain.entity.Payment;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
import com.fiap.techchallenge.interfaces.dto.OrderCreationRequest;
import com.fiap.techchallenge.interfaces.dto.OrderUpdateRequest;
import com.fiap.techchallenge.interfaces.dto.SearchOrderByIdCommand;
import com.fiap.techchallenge.interfaces.dto.SearchOrderCommand;
import com.fiap.techchallenge.interfaces.dto.UpdateOrderCommand;
import com.fiap.techchallenge.interfaces.mapper.OrderRestMapper;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UpdateOrderUseCase updateUseCase;

    private final SearchOrderUseCase searchUseCase;

    private final CreateOrderCheckoutUseCase createUseCase;

    private final SearchPaymentUseCase searchPaymentUseCase;

    private final UpdatePaymentUseCase updatePaymentUseCase;

    private final OrderRestMapper restMapper;

    @GetMapping
    ResponseEntity<List<Order>> search(
        @RequestParam(required = false) String cpf,
        @RequestParam(required = false) OrderStatus status) {
        var domainEntities = searchUseCase.searchOrders(
            new SearchOrderCommand(cpf, status));

        return ResponseEntity.ok(domainEntities);
    }

    @GetMapping("/{id}")
    ResponseEntity<Order> searchById(
        @PathVariable String id) throws ResourceNotFoundException {
        var domainEntity = searchUseCase.searchOrderById(
            new SearchOrderByIdCommand(id));

        return ResponseEntity.ok(domainEntity);
    }

    @PutMapping("/{id}")
    ResponseEntity<Order> update(@PathVariable String id,
        @RequestBody @Valid OrderUpdateRequest updateRequest) {
        try {
            var domainEntity = updateUseCase.updateOrder(
                new UpdateOrderCommand(id, updateRequest.status()));

            return new ResponseEntity<>(domainEntity, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    ResponseEntity<String> checkout(
        @RequestBody @Valid OrderCreationRequest creationRequest) {
        var domainEntity = createUseCase.createOrderCheckout(
            restMapper.toDomainEntity(creationRequest));

        return new ResponseEntity<>(domainEntity.getId(), HttpStatus.CREATED);
    }

    @PostMapping("/payments")
    ResponseEntity paymentWebhook(
        @RequestBody @Valid PaymentWebhookUpdateRequest webhookUpdateRequest) {
        updatePaymentUseCase.paymentWebhook(
            webhookUpdateRequest.paymentId(), webhookUpdateRequest.success());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payments/{id}")
    ResponseEntity<Payment> searchOrderPaymentStatus(
        @PathVariable String id) throws ResourceNotFoundException {
        var domainEntity = searchPaymentUseCase.searchPayment(id);

        return ResponseEntity.ok(domainEntity);
    }
}
