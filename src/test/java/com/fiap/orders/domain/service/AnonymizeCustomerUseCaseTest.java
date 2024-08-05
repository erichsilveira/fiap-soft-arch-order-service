package com.fiap.orders.domain.service;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnonymizeCustomerUseCaseTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private AnonymizeCustomerUseCaseImpl anonymizeCustomerUseCase;

    @Test
    void shouldAnonymizeCustomerData() {
        // Arrange
        String cpf = "12345678901";
        Customer customer = Customer.builder()
                .id("1")
                .cpf(cpf)
                .name("John Doe")
                .email("johndoe@example.com")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();

        when(repository.findById(cpf)).thenReturn(customer);

        // Act
        anonymizeCustomerUseCase.anonymizeCustomer(cpf);

        // Assert
        assertNotEquals("John Doe", customer.getName());
        assertNotEquals("johndoe@example.com", customer.getEmail());
        verify(repository).findById(cpf);
        verify(repository).registerCustomer(customer);
    }
}