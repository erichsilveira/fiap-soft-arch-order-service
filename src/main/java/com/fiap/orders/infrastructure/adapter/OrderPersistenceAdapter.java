package com.fiap.orders.infrastructure.adapter;

import com.fiap.orders.domain.entity.Order;
import com.fiap.orders.domain.entity.OrderStatus;
import com.fiap.orders.domain.repository.OrderRepository;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.infrastructure.model.OrderModel;
import com.fiap.orders.infrastructure.persistence.OrderMongoRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderPersistenceAdapter implements OrderRepository {

    private final OrderMongoRepository repository;

    @Override
    public List<Order> searchOrders(String cpf, OrderStatus status) {
        var filter = OrderModel.builder();
        if (StringUtils.isNotBlank(cpf)) {
            filter.customerCpf(cpf);
        }
        if (status != null) {
            filter.status(status);
        }
        var entities = repository.findAll(Example.of(filter.build()),
                Sort.by(Sort.Direction.DESC, "id"));
        return entities.stream()
                .map(OrderModel::toOrder).toList();
    }

    @Override
    public Order searchOrderById(String entityId) throws ResourceNotFoundException {
        var model = repository.findById(entityId)
                .orElseThrow(ResourceNotFoundException::new);
        return OrderModel.toOrder(model);
    }

    @Override
    public Order updateOrder(String entityId, OrderStatus status)
            throws ResourceNotFoundException {
        var model = repository.findById(entityId)
                .orElseThrow(ResourceNotFoundException::new);
        model.setStatus(status);
        model.setUpdatedAt(LocalDateTime.now());
        repository.save(model);
        return OrderModel.toOrder(model);
    }

    @Override
    public Order createOrder(Order order) {
        var model = OrderModel.toOrderModel(order);
        model.setStatus(OrderStatus.WAITING_PAYMENT);
        model.setCreatedAt(LocalDateTime.now());
        model.setUpdatedAt(LocalDateTime.now());
        repository.save(model);
        return OrderModel.toOrder(model);
    }
}
