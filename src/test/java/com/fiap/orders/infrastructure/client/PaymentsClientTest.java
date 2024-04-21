package com.fiap.orders.infrastructure.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.interfaces.dto.PaymentsStatusResponse;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentsClientTest {
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentsClient paymentsClient;

    private MockClient mockClient;

    @BeforeEach
    void setUp() {
        mockClient = MockClient.register();
        Unirest.config().setObjectMapper(new JacksonObjectMapper(objectMapper)); // Use mocked ObjectMapper in Unirest
        paymentsClient.paymentsUrl = "http://fake-url.com";
    }

    @Test
    void testGetPaymentsByOrder() throws Exception {
        String orderId = "order123";
        String jsonResponse = "[{\"id\":\"1\",\"orderId\":\"order123\",\"orderPrice\":100.00,\"status\":\"APPROVED\",\"createdAt\":\"2021-01-01T12:00:00\",\"updatedAt\":\"2021-01-01T12:00:00\"}]";

        mockClient.expect(HttpMethod.GET, "http://fake-url.com")
                .queryString("orderId", orderId)
                .thenReturn(MockResponse.of(200, jsonResponse));

        when(objectMapper.readValue(anyString(), any(TypeReference.class)))
                .thenReturn(List.of(new PaymentsStatusResponse("1", "order123", new BigDecimal("100.00"), "APPROVED", LocalDateTime.parse("2021-01-01T12:00:00"), LocalDateTime.parse("2021-01-01T12:00:00"))));

        List<PaymentsStatusResponse> results = paymentsClient.getPaymentsByOrder(orderId);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals("APPROVED", results.get(0).status());

        mockClient.verifyAll();
    }

    @Test
    void testGetPaymentsByOrderWhenEmpty() throws Exception {
        String orderId = "order123";
        mockClient.expect(HttpMethod.GET, "http://fake-url.com")
                .queryString("orderId", orderId)
                .thenReturn(MockResponse.of(404, "not found")); // Simulate an empty JSON array response

        List<PaymentsStatusResponse> results = paymentsClient.getPaymentsByOrder(orderId);

        assertTrue(results.isEmpty());
        mockClient.verifyAll();
    }

    @BeforeEach
    void tearDown() {
        Unirest.shutDown();
    }
}
