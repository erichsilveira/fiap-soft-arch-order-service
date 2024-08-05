package com.fiap.orders.domain.service;

import com.fiap.orders.application.usecases.AnonymizeCustomerUseCase;
import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.domain.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class AnonymizeCustomerUseCaseImpl implements AnonymizeCustomerUseCase {

    private final CustomerRepository repository;
    private final Random random;

    public AnonymizeCustomerUseCaseImpl(CustomerRepository repository) {
        this.repository = repository;
        this.random = new Random();
    }

    public void anonymizeCustomer(String cpf) {
        Customer customer = repository.findById(cpf);
        customer.setCpf(shuffleString(cpf));
        customer.setName(shuffleString(customer.getName()));
        customer.setEmail(shuffleString(customer.getEmail()));

        log.debug("Anonymizing customer with CPF: {} - {}", cpf, customer);
        repository.registerCustomer(customer);
    }

    private String shuffleString(String input) {
        char[] characters = input.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            int randomIndex = random.nextInt(characters.length);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
}
