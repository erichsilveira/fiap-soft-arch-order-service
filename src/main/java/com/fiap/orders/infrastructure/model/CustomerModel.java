package com.fiap.orders.infrastructure.model;

import com.fiap.orders.domain.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document("customers")
public class CustomerModel {


    public static CustomerModel toCustomerModel(Customer customer) {
        return CustomerModel.builder()
                .id(customer.getId())
                .cpf(customer.getCpf())
                .name(customer.getName())
                .email(customer.getEmail())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }

    public static Customer toCustomer(CustomerModel customerModel) {
        return Customer.builder()
                .id(customerModel.getId())
                .cpf(customerModel.getCpf())
                .name(customerModel.getName())
                .email(customerModel.getEmail())
                .createdAt(customerModel.getCreatedAt())
                .updatedAt(customerModel.getUpdatedAt())
                .build();
    }

    @Id
    private String id;

    private String cpf;

    private String name;

    private String email;

    private ZonedDateTime createdAt;
    
    private ZonedDateTime updatedAt;
}
