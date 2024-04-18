package com.fiap.orders.infrastructure.persistence;

import com.fiap.techchallenge.infrastructure.model.CustomerModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends CrudRepository<CustomerModel, String> {

    boolean existsByCpf(String cpf);

}
