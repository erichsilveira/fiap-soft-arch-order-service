package com.fiap.orders.infrastructure.adapter;

import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.infrastructure.model.CustomerModel;
import com.fiap.orders.infrastructure.persistence.CustomerMongoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerPersistenceAdapterTest {

    @Mock
    private CustomerMongoRepository customerMongoRepository;

    @InjectMocks
    private CustomerPersistenceAdapter customerPersistenceAdapter;

    private Customer customer;
    private CustomerModel customerModel;

    @BeforeEach
    void setUp() {
        ZonedDateTime now = ZonedDateTime.now();
        customer = Customer.builder()
                .id("1")
                .cpf("12345678901")
                .name("John Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();
        customerModel = CustomerModel.builder()
                .id("1")
                .cpf("12345678901")
                .name("John Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void shouldRegisterCustomerSuccessfully() {
        when(customerMongoRepository.save(any(CustomerModel.class))).thenReturn(customerModel);

        Customer registeredCustomer = customerPersistenceAdapter.registerCustomer(customer);

        assertNotNull(registeredCustomer);
        assertEquals(customer.getCpf(), registeredCustomer.getCpf());
        verify(customerMongoRepository).save(any(CustomerModel.class));
    }

    @Test
    void shouldExistCustomer() {
        String cpf = "12345678901";
        when(customerMongoRepository.existsByCpf(cpf)).thenReturn(true);

        boolean exists = customerPersistenceAdapter.existsCustomer(cpf);

        assertTrue(exists);
        verify(customerMongoRepository).existsByCpf(cpf);
    }

    @Test
    void shouldNotExistCustomer() {
        String cpf = "98765432109";
        when(customerMongoRepository.existsByCpf(cpf)).thenReturn(false);

        boolean exists = customerPersistenceAdapter.existsCustomer(cpf);

        assertFalse(exists);
        verify(customerMongoRepository).existsByCpf(cpf);
    }
}
