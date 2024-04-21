package com.fiap.orders.domain.entity;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Customer {

    private final String id;

    private final String cpf;

    private final String name;

    private final String email;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;
}
