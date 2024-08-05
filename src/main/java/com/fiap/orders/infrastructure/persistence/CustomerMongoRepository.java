package com.fiap.orders.infrastructure.persistence;

import com.fiap.orders.infrastructure.model.CustomerModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerModel, String> {

    boolean existsByCpf(String cpf);

    CustomerModel findByCpf(String cpf);
}
