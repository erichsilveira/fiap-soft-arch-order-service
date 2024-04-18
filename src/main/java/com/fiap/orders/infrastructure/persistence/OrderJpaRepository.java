package com.fiap.orders.infrastructure.persistence;

import com.fiap.techchallenge.infrastructure.model.OrderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderModel, String> {

}
