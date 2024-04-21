package com.fiap.orders.domain.service;

import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.infrastructure.client.ProductionClient;
import com.fiap.orders.interfaces.dto.ProductDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckProductsUseCaseTest {

    @Mock
    private ProductionClient productionClient;

    @InjectMocks
    private CheckProductsUseCaseImpl checkProductsUseCase;

    private List<String> productIds;
    private ProductDataResponse productResponse;

    @BeforeEach
    void setUp() {
        productIds = Arrays.asList("prod123", "prod456");
        productResponse = new ProductDataResponse(
                "prod123",
                "Product 123",
                "This is a valid product",
                new BigDecimal("12.99"),
                "category123",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void shouldProcessSuccessfullyWhenProductExists() {
        // Arrange
        when(productionClient.getProductById("prod123")).thenReturn(productResponse);
        when(productionClient.getProductById("prod456")).thenReturn(null);

        // Act
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            checkProductsUseCase.checkProducts(productIds);
        });

        // Assert
        assertNotNull(exception);
        verify(productionClient).getProductById("prod123");
        verify(productionClient).getProductById("prod456");
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductNotFound() {
        // Arrange
        when(productionClient.getProductById("prod123")).thenReturn(null);

        // Act
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            checkProductsUseCase.checkProducts(Arrays.asList("prod123"));
        });

        // Assert
        assertNotNull(thrown);
        verify(productionClient).getProductById("prod123");
    }
}
