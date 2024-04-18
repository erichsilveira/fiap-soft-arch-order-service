package com.fiap.orders.infrastructure.adapter;

import com.fiap.techchallenge.domain.entity.Customer;
import com.fiap.techchallenge.domain.repository.CustomerRepository;
import com.fiap.techchallenge.infrastructure.model.CustomerModel;
import com.fiap.techchallenge.infrastructure.persistence.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomerPersistenceAdapter implements CustomerRepository {

    private final CustomerJpaRepository springDataRepository;

    @Override
    public Customer registerCustomer(Customer customer) {
        CustomerModel model = CustomerModel.toCustomerModel(customer);

        springDataRepository.save(model);

        return CustomerModel.toCustomer(model);
    }

    @Override
    public boolean existsCustomer(String cpf) {
        return springDataRepository.existsByCpf(cpf);
    }
}
