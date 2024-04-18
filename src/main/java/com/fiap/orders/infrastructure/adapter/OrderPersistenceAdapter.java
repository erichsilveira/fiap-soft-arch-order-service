package com.fiap.orders.infrastructure.adapter;

import com.fiap.techchallenge.domain.entity.Order;
import com.fiap.techchallenge.domain.entity.OrderStatus;
import com.fiap.techchallenge.domain.repository.OrderRepository;
import com.fiap.techchallenge.exception.ResourceNotFoundException;
import com.fiap.techchallenge.infrastructure.model.OrderModel;
import com.fiap.techchallenge.infrastructure.persistence.OrderJpaRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderPersistenceAdapter implements OrderRepository {

    private final OrderJpaRepository springDataRepository;

    @Override
    public List<Order> searchOrders(String cpf, OrderStatus status) {
        var filter = OrderModel.builder();

        if (StringUtils.isNotBlank(cpf)) {
            filter.customerCpf(cpf);
        }

        if (status != null) {
            filter.status(status);
        }

        var entities = springDataRepository.findAll(Example.of(filter.build()),
                Sort.by(Sort.Direction.DESC, "id"));

        return entities.stream()
                .map(OrderModel::toOrder).toList();
    }

    @Override
    public Order searchOrderById(String entityId) throws ResourceNotFoundException {
        var model = springDataRepository.findById(entityId)
                .orElseThrow(ResourceNotFoundException::new);

        return OrderModel.toOrder(model);
    }

    @Override
    public Order updateOrder(String entityId, OrderStatus status)
            throws ResourceNotFoundException {
        var model = springDataRepository.findById(entityId)
                .orElseThrow(ResourceNotFoundException::new);

        model.setStatus(status);

        springDataRepository.save(model);

        return OrderModel.toOrder(model);
    }

    @Override
    public Order createOrder(Order order) {
        var model = OrderModel.toOrderModel(order);
        var products = model.getProducts();
        model.setProducts(null);

        model.setStatus(OrderStatus.WAITING_PAYMENT);

        springDataRepository.save(model);

        products.forEach(product -> {
            product.setOrder(OrderModel.builder().id(model.getId()).build());
        });

        model.setProducts(new ArrayList<>(products));
        springDataRepository.save(model);

        return OrderModel.toOrder(model);
    }
}
