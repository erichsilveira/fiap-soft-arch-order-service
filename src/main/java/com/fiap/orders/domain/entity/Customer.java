package com.fiap.orders.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

@Getter
@Builder
@ToString
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
