package com.fiap.orders.interfaces.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentsStatusResponse(String id,
                                     String orderId,
                                     BigDecimal orderPrice,
                                     String status,
                                     LocalDateTime createdAt,
                                     LocalDateTime updatedAt) {
}
