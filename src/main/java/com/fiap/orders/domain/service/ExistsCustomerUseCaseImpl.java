package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.ExistsCustomerUseCase;
import com.fiap.orders.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExistsCustomerUseCaseImpl implements ExistsCustomerUseCase {

    private final CustomerRepository repository;

    @Override
    public boolean existsCustomer(String cpf) {
        return repository.existsCustomer(cpf);
    }

}
