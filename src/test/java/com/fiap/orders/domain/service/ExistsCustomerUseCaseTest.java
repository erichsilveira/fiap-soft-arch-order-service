package com.fiap.orders.domain.service;

import com.fiap.orders.domain.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExistsCustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private ExistsCustomerUseCaseImpl existsCustomerUseCase;

    @Test
    void shouldReturnTrueWhenCustomerExists() {
        // Arrange
        String cpf = "12345678901";
        when(customerRepository.existsCustomer(cpf)).thenReturn(true);

        // Act
        boolean exists = existsCustomerUseCase.existsCustomer(cpf);

        // Assert
        assertTrue(exists);
        verify(customerRepository).existsCustomer(cpf);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExist() {
        // Arrange
        String cpf = "12345678901";
        when(customerRepository.existsCustomer(cpf)).thenReturn(false);

        // Act
        boolean exists = existsCustomerUseCase.existsCustomer(cpf);

        // Assert
        assertFalse(exists);
        verify(customerRepository).existsCustomer(cpf);
    }
}
