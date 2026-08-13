package com.nexora.erp.stock.controller;

import com.nexora.erp.product.entity.Product;
import com.nexora.erp.product.repository.ProductRepository;
import com.nexora.erp.stock.repository.StockMovementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static com.nexora.erp.support.SecurityMockMvc.employee;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockMovementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @BeforeEach
    void setUp() {
        stockMovementRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void shouldRegisterInboundMovementAndIncreaseStock() throws Exception {
        Product product = saveProduct(10);

        mockMvc.perform(post("/api/stock-movements")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": %d,
                                  "type": "ENTRADA",
                                  "quantity": 5,
                                  "reason": "Compra de mercadorias"
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/stock-movements/")))
                .andExpect(jsonPath("$.type").value("ENTRADA"))
                .andExpect(jsonPath("$.quantity").value(5));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(15);
    }

    @Test
    void shouldRegisterOutboundMovementAndDecreaseStock() throws Exception {
        Product product = saveProduct(10);

        mockMvc.perform(post("/api/stock-movements")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": %d,
                                  "type": "SAIDA",
                                  "quantity": 4,
                                  "reason": "Ajuste de estoque"
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("SAIDA"))
                .andExpect(jsonPath("$.quantity").value(4));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(6);
    }

    @Test
    void shouldNotRegisterOutboundMovementWhenStockIsInsufficient() throws Exception {
        Product product = saveProduct(3);

        mockMvc.perform(post("/api/stock-movements")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": %d,
                                  "type": "SAIDA",
                                  "quantity": 4,
                                  "reason": "Ajuste de estoque"
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Nao ha estoque suficiente para realizar a saida."));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(3);
        assertThat(stockMovementRepository.count()).isZero();
    }

    @Test
    void shouldReturnBadRequestWhenQuantityIsInvalid() throws Exception {
        Product product = saveProduct(10);

        mockMvc.perform(post("/api/stock-movements")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": %d,
                                  "type": "ENTRADA",
                                  "quantity": 0,
                                  "reason": "Compra de mercadorias"
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Existem campos invalidos na requisicao."));
    }

    @Test
    void shouldListMovementsByProduct() throws Exception {
        Product product = saveProduct(10);

        mockMvc.perform(post("/api/stock-movements")
                        .with(employee())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "productId": %d,
                                  "type": "ENTRADA",
                                  "quantity": 2,
                                  "reason": "Compra de mercadorias"
                                }
                                """.formatted(product.getId())))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/products/" + product.getId() + "/stock-movements")
                        .with(employee()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].productId").value(product.getId()))
                .andExpect(jsonPath("$.content[0].type").value("ENTRADA"));
    }

    private Product saveProduct(Integer stockQuantity) {
        Product product = new Product(
                "Produto teste",
                "Produto usado nos testes de estoque.",
                "STOCK-" + stockQuantity,
                new BigDecimal("49.90"),
                stockQuantity,
                2
        );

        return productRepository.save(product);
    }
}
