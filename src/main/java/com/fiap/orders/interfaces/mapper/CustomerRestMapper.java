package com.fiap.orders.interfaces.mapper;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.interfaces.dto.CustomerRegistrationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomerRestMapper {

    public Customer toDomainEntity(CustomerRegistrationRequest restEntity) {
        return Customer.builder().cpf(restEntity.cpf())
            .name(restEntity.name()).email(restEntity.email()).build();
    }
}
