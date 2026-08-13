package com.nexora.erp.product.controller;

import com.nexora.erp.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Mouse sem fio",
                                  "description": "Mouse ergonomico para escritorio.",
                                  "sku": "mouse-001",
                                  "price": 89.90,
                                  "stockQuantity": 10,
                                  "minimumStock": 3
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/products/")))
                .andExpect(jsonPath("$.name").value("Mouse sem fio"))
                .andExpect(jsonPath("$.sku").value("MOUSE-001"))
                .andExpect(jsonPath("$.active").value(true))
                .andExpect(jsonPath("$.lowStock").value(false));
    }

    @Test
    void shouldReturnConflictWhenSkuAlreadyExists() throws Exception {
        String requestBody = """
                {
                  "name": "Mouse sem fio",
                  "description": "Mouse ergonomico para escritorio.",
                  "sku": "mouse-001",
                  "price": 89.90,
                  "stockQuantity": 10,
                  "minimumStock": 3
                }
                """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ja existe um produto cadastrado com este SKU."));
    }

    @Test
    void shouldReturnBadRequestWhenPriceIsInvalid() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Mouse sem fio",
                                  "description": "Mouse ergonomico para escritorio.",
                                  "sku": "mouse-001",
                                  "price": 0,
                                  "stockQuantity": 10,
                                  "minimumStock": 3
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Existem campos invalidos na requisicao."));
    }

    @Test
    void shouldFindLowStockProducts() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Teclado compacto",
                                  "description": "Teclado para escritorio.",
                                  "sku": "tec-001",
                                  "price": 129.90,
                                  "stockQuantity": 2,
                                  "minimumStock": 5
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].sku").value("TEC-001"))
                .andExpect(jsonPath("$.content[0].lowStock").value(true));
    }

    @Test
    void shouldDeactivateProduct() throws Exception {
        String location = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Monitor 24",
                                  "description": "Monitor LED.",
                                  "sku": "mon-024",
                                  "price": 799.90,
                                  "stockQuantity": 4,
                                  "minimumStock": 1
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
