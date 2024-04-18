package com.fiap.orders.domain.repository;


import com.fiap.techchallenge.domain.entity.Customer;

public interface CustomerRepository {

    boolean existsCustomer(String cpf);

    Customer registerCustomer(Customer customer);
}
