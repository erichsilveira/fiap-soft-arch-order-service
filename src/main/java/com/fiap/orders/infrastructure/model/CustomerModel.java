package com.fiap.orders.infrastructure.model;

import com.fiap.techchallenge.domain.entity.Customer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "customers")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @GeneratedValue
    @UuidGenerator
    private String id;

    private String cpf;

    private String name;

    private String email;

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
