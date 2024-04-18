package com.fiap.orders.application.usecases;

import com.fiap.techchallenge.domain.entity.Customer;

public interface RegisterCustomerUseCase {

    Customer registerCustomer(Customer command);
}
