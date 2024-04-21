package com.fiap.orders.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.interfaces.dto.ProductDataResponse;
import kong.unirest.core.HttpMethod;
import kong.unirest.core.MockClient;
import kong.unirest.core.MockResponse;
import kong.unirest.core.Unirest;
import kong.unirest.modules.jackson.JacksonObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionClientTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ProductionClient productionClient;

    private MockClient mockClient;

    @BeforeEach
    void setUp() {
        mockClient = MockClient.register();
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)); // Set custom ObjectMapper
        productionClient.productionUrl = "http://fake-url.com";
    }

    @Test
    void getProductByIdSuccess() throws Exception {
        String productId = "product123";
        String jsonResponse = "{\"id\":\"product123\",\"name\":\"Test Product\",\"price\":50.00,\"createdAt\":\"2021-01-01T12:00:00\",\"updatedAt\":\"2021-01-01T12:00:00\"}";

        mockClient.expect(HttpMethod.GET, "http://fake-url.com/product123")
                .thenReturn(MockResponse.of(200, jsonResponse));

        when(objectMapper.readValue(jsonResponse, ProductDataResponse.class))
                .thenReturn(new ProductDataResponse("123", "Test Product", null, new BigDecimal("50.00"), "prodCat123", LocalDateTime.parse("2021-01-01T12:00:00"), LocalDateTime.parse("2021-01-01T12:00:00")));

        ProductDataResponse result = productionClient.getProductById(productId);

        assertNotNull(result);
        assertEquals("123", result.id());
        assertEquals("Test Product", result.name());

        mockClient.verifyAll();
    }

    @Test
    void getProductByIdFailure() {
        String productId = "unknown";
        mockClient.expect(HttpMethod.GET, "http://fake-url.com/unknown")
                .thenReturn(MockResponse.of(404, "not found"));

        ProductDataResponse result = productionClient.getProductById(productId);

        assertNull(result);
        mockClient.verifyAll();
    }

    @Test
    void getProductByIdSuccessWithException() throws Exception {
        String productId = "product123";
        String jsonResponse = "{\"id\":\"product123\",\"name\":\"Test Product\",\"price\":50.00,\"createdAt\":\"2021-01-01T12:00:00\",\"updatedAt\":\"2021-01-01T12:00:00\"}";

        mockClient.expect(HttpMethod.GET, "http://fake-url.com/product123")
                .thenReturn(MockResponse.of(200, jsonResponse));

        when(objectMapper.readValue(jsonResponse, ProductDataResponse.class))
                .thenThrow(new RuntimeException("Error parsing JSON"));

        ProductDataResponse result = productionClient.getProductById(productId);

        assertNull(result); // Verificar que o resultado é nulo devido à exceção

        mockClient.verifyAll();
    }


    @BeforeEach
    void tearDown() {
        Unirest.shutDown();
    }
}
