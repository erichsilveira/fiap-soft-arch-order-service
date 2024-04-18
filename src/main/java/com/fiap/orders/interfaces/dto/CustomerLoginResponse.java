package com.fiap.orders.interfaces.dto;

public record CustomerLoginResponse(String token, Long exp, String sub) {

}
