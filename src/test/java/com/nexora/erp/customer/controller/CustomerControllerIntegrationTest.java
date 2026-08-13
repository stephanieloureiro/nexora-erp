package com.nexora.erp.customer.controller;

import com.nexora.erp.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ana Silva",
                                  "document": "123.456.789-00",
                                  "email": "ana@email.com",
                                  "phone": "11999999999"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/customers/")))
                .andExpect(jsonPath("$.name").value("Ana Silva"))
                .andExpect(jsonPath("$.document").value("12345678900"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnConflictWhenDocumentAlreadyExists() throws Exception {
        String requestBody = """
                {
                  "name": "Ana Silva",
                  "document": "123.456.789-00",
                  "email": "ana@email.com",
                  "phone": "11999999999"
                }
                """;

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe um cliente cadastrado com este CPF ou CNPJ."));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ana Silva",
                                  "document": "123.456.789-00",
                                  "email": "email-invalido",
                                  "phone": "11999999999"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Existem campos invalidos na requisicao."));
    }

    @Test
    void shouldFindCustomerById() throws Exception {
        String location = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Maria Souza",
                                  "document": "987.654.321-00",
                                  "email": "maria@email.com",
                                  "phone": "11888888888"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria Souza"));
    }

    @Test
    void shouldDeactivateCustomer() throws Exception {
        String location = mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Carlos Lima",
                                  "document": "111.222.333-44",
                                  "email": "carlos@email.com",
                                  "phone": "11777777777"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");

        mockMvc.perform(patch(location + "/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }
}
