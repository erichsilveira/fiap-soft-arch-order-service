package com.fiap.orders.domain.service;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RegisterCustomerUseCaseImpl registerCustomerUseCase;

    private Customer customer;

    @BeforeEach
    void setUp() {
        // Setup a sample customer
        customer = Customer.builder()
                .id("1")
                .cpf("12345678901")
                .name("John Doe")
                .email("john.doe@example.com")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }

    @Test
    void shouldRegisterCustomerSuccessfully() {
        // Arrange
        when(customerRepository.registerCustomer(any(Customer.class))).thenReturn(customer);

        // Act
        Customer registeredCustomer = registerCustomerUseCase.registerCustomer(customer);

        // Assert
        assertNotNull(registeredCustomer);
        assertEquals(customer.getCpf(), registeredCustomer.getCpf());
        verify(customerRepository).registerCustomer(customer);
    }
}
