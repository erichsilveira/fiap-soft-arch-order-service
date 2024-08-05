package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.CheckProductsUseCase;
import com.fiap.orders.exception.ResourceNotFoundException;
import com.fiap.orders.infrastructure.client.ProductionClient;
import com.fiap.orders.interfaces.dto.ProductDataResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CheckProductsUseCaseImpl implements CheckProductsUseCase {

    private ProductionClient productionClient;


    @Override
    public void checkProducts(List<String> productIds) {
        log.info("Checking products: {}", productIds);

        productIds.forEach(productId -> {
            ProductDataResponse product = productionClient.getProductById(productId);
            if (Objects.nonNull(product)) {
                log.info("Product {} found", product.id());
            } else {
                log.info("Product {} not found", productId);
                throw new ResourceNotFoundException("Product not found");
            }

        });
    }
}
