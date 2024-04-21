package com.fiap.orders.mocks;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.infrastructure.model.CustomerModel;

import java.time.ZonedDateTime;
import java.util.UUID;

public class CustomerMock {

    public static CustomerModel generateCustomerModel(String id, String cpf, String name, String email) {
        return CustomerModel.builder()
                .id(id)
                .cpf(cpf)
                .name(name)
                .email(email)
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }

    public static CustomerModel generateCustomerModel() {
        String id = UUID.randomUUID().toString();
        String cpf = "123.456.789-09";
        String name = "John Doe";
        String email = "johndoe@example.com";
        return generateCustomerModel(id, cpf, name, email);
    }

    public static Customer generateCustomer(String id, String cpf, String name, String email) {
        return CustomerModel.toCustomer(generateCustomerModel(id, cpf, name, email));
    }

    public static Customer generateCustomer() {
        CustomerModel model = generateCustomerModel();
        return CustomerModel.toCustomer(model);
    }
}
