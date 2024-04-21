package com.fiap.orders.infrastructure.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.interfaces.dto.PaymentsStatusResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PaymentsClient {

    @Value("${payments.service.url}")
    private String paymentsUrl;

    @Autowired
    private ObjectMapper objectMapper;

    public List<PaymentsStatusResponse> getPaymentsByOrder(String orderId) {
        HttpResponse<String> response = Unirest.get(paymentsUrl)
                .header("Content-Type", "application/json")
                .queryString("orderId", orderId)
                .asString();

        if (response.getStatus() == 200) {
            try {
                // Deserialize JSON into a List of PaymentsStatusResponse
                return objectMapper.readValue(response.getBody(), new TypeReference<List<PaymentsStatusResponse>>() {
                });
            } catch (Exception e) {
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        }

        // Return an empty list in case of non-200 responses or parsing failures
        return Collections.emptyList();
    }
}
