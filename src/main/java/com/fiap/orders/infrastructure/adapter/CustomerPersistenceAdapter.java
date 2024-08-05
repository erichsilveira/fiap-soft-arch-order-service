package com.fiap.orders.infrastructure.adapter;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.domain.repository.CustomerRepository;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.infrastructure.model.CustomerModel;
import com.fiap.orders.infrastructure.persistence.CustomerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerPersistenceAdapter implements CustomerRepository {

    private final CustomerMongoRepository repository;

    @Override
    public Customer registerCustomer(Customer customer) {
        CustomerModel model = CustomerModel.toCustomerModel(customer);
        repository.save(model);
        return CustomerModel.toCustomer(model);
    }

    @Override
    public boolean existsCustomer(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public Customer findById(String cpf) {
        try {
            return CustomerModel.toCustomer(repository.findByCpf(cpf));
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }
    }
}
