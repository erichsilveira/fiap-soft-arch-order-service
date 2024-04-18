package com.fiap.orders.domain.entity;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
public class Customer {

    @Getter
    private final String id;

    @Getter
    private final String cpf;

    @Getter
    private final String name;

    @Getter
    private final String email;

    @Getter
    private ZonedDateTime createdAt;

    @Getter
    private ZonedDateTime updatedAt;
}
