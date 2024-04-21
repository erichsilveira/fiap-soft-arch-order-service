package com.fiap.orders.interfaces.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerRegistrationRequest(@NotBlank String cpf, @NotBlank String name, @NotBlank String email) {

}
