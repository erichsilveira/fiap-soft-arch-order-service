package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Builder
public class Customer {

    private final String id;

    @Setter
    private String cpf;

    @Setter
    private String name;

    @Setter
    private String email;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
