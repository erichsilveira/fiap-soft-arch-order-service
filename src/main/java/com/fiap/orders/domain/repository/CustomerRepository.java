package com.fiap.orders.domain.repository;


import com.fiap.orders.domain.entity.Customer;

public interface CustomerRepository {

    boolean existsCustomer(String cpf);

    Customer registerCustomer(Customer customer);

    Customer findById(String cpf);
}
