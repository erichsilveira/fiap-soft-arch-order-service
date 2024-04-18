package com.fiap.orders.domain.service;

import com.fiap.techchallenge.application.usecases.RegisterCustomerUseCase;
import com.fiap.techchallenge.domain.entity.Customer;
import com.fiap.techchallenge.domain.repository.CustomerRepository;
import com.fiap.techchallenge.infrastructure.model.CustomerModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RegisterCustomerUseCaseImpl implements RegisterCustomerUseCase {

    private final CustomerRepository repository;

    @Override
    public Customer registerCustomer(Customer customer) {
        log.info("Registering customer with cpf {}", customer.getCpf());
        return repository.registerCustomer(customer);
    }
}
