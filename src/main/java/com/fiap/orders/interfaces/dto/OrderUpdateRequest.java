package com.fiap.orders.interfaces.dto;

import com.fiap.techchallenge.domain.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderUpdateRequest(@NotNull OrderStatus status) {

}
