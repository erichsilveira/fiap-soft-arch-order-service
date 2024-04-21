package com.fiap.orders.infrastructure.persistence;

import com.fiap.orders.infrastructure.model.OrderModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderModel, String> {
}