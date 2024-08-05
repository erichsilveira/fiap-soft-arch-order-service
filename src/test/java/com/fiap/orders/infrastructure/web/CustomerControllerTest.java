package com.fiap.orders.infrastructure.web;

import com.fiap.orders.application.usecases.AnonymizeCustomerUseCase;
import com.fiap.orders.application.usecases.ExistsCustomerUseCase;
import com.fiap.orders.application.usecases.RegisterCustomerUseCase;
import com.fiap.orders.domain.entity.Customer;
import com.fiap.orders.infrastructure.persistence.CustomerMongoRepository;
import com.fiap.orders.infrastructure.persistence.OrderMongoRepository;
import com.fiap.orders.interfaces.dto.CustomerRegistrationRequest;
import com.fiap.orders.interfaces.mapper.CustomerRestMapper;
import com.fiap.orders.security.JwtUtil;
import com.fiap.orders.security.JwtUtil.JwtToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
@Import(CustomerRestMapper.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRestMapper customerRestMapper;

    @MockBean
    private RegisterCustomerUseCase registerCustomerUseCase;

    @MockBean
    private ExistsCustomerUseCase existsCustomerUseCase;

    @MockBean
    private AnonymizeCustomerUseCase anonymizeCustomerUseCase;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomerMongoRepository customerMongoRepository;

    @MockBean
    private OrderMongoRepository orderMongoRepository;

    @Test
    public void registerCustomerSuccess() throws Exception {
        Customer customer = Customer.builder()
                .id("1")
                .cpf("12345678900")
                .name("John Doe")
                .email("johndoe@example.com")
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("12345678900", "John Doe", "johndoe@example.com");

        given(registerCustomerUseCase.registerCustomer(any())).willReturn(customer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"12345678900\",\"name\":\"John Doe\",\"email\":\"johndoe@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void customerExists() throws Exception {
        given(existsCustomerUseCase.existsCustomer("12345678900")).willReturn(true);

        mockMvc.perform(head("/customers?cpf=12345678900"))
                .andExpect(status().isOk());
    }

    @Test
    public void customerDoesNotExist() throws Exception {
        given(existsCustomerUseCase.existsCustomer("12345678900")).willReturn(false);

        mockMvc.perform(head("/customers?cpf=12345678900"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void customerLoginSuccess() throws Exception {
        given(existsCustomerUseCase.existsCustomer("12345678900")).willReturn(true);

        JwtToken jwtToken = new JwtToken("token123", ZonedDateTime.now().plusHours(1).toEpochSecond(), "12345678900");
        given(jwtUtil.createToken("12345678900")).willReturn(jwtToken);

        mockMvc.perform(post("/customers/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"12345678900\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("token123")))
                .andExpect(jsonPath("$.exp").exists())
                .andExpect(jsonPath("$.sub", is("12345678900")));
    }

    @Test
    public void customerLoginFailure() throws Exception {
        given(existsCustomerUseCase.existsCustomer("12345678900")).willReturn(false);

        mockMvc.perform(post("/customers/tokens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cpf\":\"12345678900\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void anonymizeCustomer() throws Exception {
        mockMvc.perform(post("/customers/anonymize/12345678900"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void anonymizeCustomerFailure() throws Exception {
        mockMvc.perform(post("/customers/anonymize/12345678900"))
                .andExpect(status().isNoContent());
    }
}
