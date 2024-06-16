package com.fiap.orders.infrastructure.web;

import com.fiap.orders.application.usecases.CreateOrderUseCase;
import com.fiap.orders.application.usecases.SearchOrderUseCase;
import com.fiap.orders.application.usecases.UpdateOrderUseCase;
import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.exception.ConflictException;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.interfaces.dto.*;
import com.fiap.orders.interfaces.mapper.OrderRestMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UpdateOrderUseCase updateUseCase;

    private final SearchOrderUseCase searchUseCase;

    private final CreateOrderUseCase createUseCase;

    private final OrderRestMapper restMapper;

    @GetMapping
    ResponseEntity<List<Order>> search(
            @RequestParam(required = false) @Size(min = 1, max = 11) String cpf,
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
    ResponseEntity<?> update(@PathVariable String id,
                             @RequestBody @Valid OrderUpdateRequest updateRequest) {
        try {
            var domainEntity = updateUseCase.updateOrder(
                    new UpdateOrderCommand(id, updateRequest.status()));

            return new ResponseEntity<>(domainEntity, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ConflictException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<String> create(
            @RequestBody @Valid OrderCreationRequest creationRequest) {

        try {
            var domainEntity = createUseCase.createOrder(
                    restMapper.toDomainEntity(creationRequest));
            return new ResponseEntity<>(domainEntity.getId(), HttpStatus.CREATED);
        } catch (ConflictException ex) {
            return ResponseEntity.status(409).body(ex.getMessage());
        }
    }
}
