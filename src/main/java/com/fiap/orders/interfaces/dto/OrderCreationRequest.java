package com.fiap.orders.interfaces.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreationRequest(@NotNull List<String> productsId,
                                   String customerCpf,
                                   @NotNull BigDecimal orderPrice) {

}
