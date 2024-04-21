package com.fiap.orders.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDataResponse(String id,
                                  String name,
                                  String description,
                                  BigDecimal price,
                                  String productCategoryId,
                                  LocalDateTime createdAt,
                                  LocalDateTime updatedAt) {
}
