package com.nexora.erp.audit.controller;

import com.nexora.erp.audit.repository.AuditEventRepository;
import com.nexora.erp.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.nexora.erp.support.SecurityMockMvc.admin;
import static com.nexora.erp.support.SecurityMockMvc.employee;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuditEventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditEventRepository auditEventRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        auditEventRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldRecordAndListAuditEventsForAdmin() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Cliente Auditavel",
                                  "document": "222.333.444-55",
                                  "email": "auditavel@email.com",
                                  "phone": "11922223333"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/audit-events")
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].action").value("CUSTOMER_CREATED"))
                .andExpect(jsonPath("$.content[0].entityType").value("Customer"))
                .andExpect(jsonPath("$.content[0].username").value("test@nexora.com"));
    }

    @Test
    void shouldFilterAuditEventsByEntityType() throws Exception {
        mockMvc.perform(post("/api/customers")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Cliente Auditavel",
                                  "document": "333.444.555-66",
                                  "email": "auditavel2@email.com",
                                  "phone": "11933334444"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/audit-events?entityType=Customer")
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].entityType").value("Customer"));
    }

    @Test
    void shouldDenyAuditEventsForEmployee() throws Exception {
        mockMvc.perform(get("/api/audit-events")
                        .with(employee()))
                .andExpect(status().isForbidden());
    }
}
