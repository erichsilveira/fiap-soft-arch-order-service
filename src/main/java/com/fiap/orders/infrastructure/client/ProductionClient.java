package com.fiap.orders.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.orders.interfaces.dto.ProductDataResponse;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProductionClient {

    @Value("${production.service.url}")
    private String productionUrl;

    @Autowired
    private ObjectMapper objectMapper;  // Spring-managed Jackson ObjectMapper

    public ProductDataResponse getProductById(String productId) {
        HttpResponse<String> response = Unirest.get(productionUrl + "/{productId}")
                .header("Content-Type", "application/json")
                .routeParam("productId", productId)  // Corrected from .path to .routeParam
                .asString();

        if (response.getStatus() == 200) {
            try {
                return objectMapper.readValue(response.getBody(), ProductDataResponse.class);
            } catch (Exception e) {
                // Log and handle the exception appropriately
                System.err.println("Error parsing JSON: " + e.getMessage());
            }
        } else {
            // Log and handle the error appropriately
            System.err.println("Error fetching product data: " + response.getStatus());
        }

        return null;
    }
}
