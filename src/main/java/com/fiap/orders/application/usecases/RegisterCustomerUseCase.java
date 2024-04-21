package com.fiap.orders.application.usecases;

import com.fiap.orders.domain.entity.Customer;

public interface RegisterCustomerUseCase {

    Customer registerCustomer(Customer command);
}
