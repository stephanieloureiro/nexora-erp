package com.nexora.erp.order.controller;

import com.nexora.erp.customer.entity.Customer;
import com.nexora.erp.customer.repository.CustomerRepository;
import com.nexora.erp.order.repository.SalesOrderRepository;
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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SalesOrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SalesOrderRepository salesOrderRepository;

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @BeforeEach
    void setUp() {
        stockMovementRepository.deleteAll();
        salesOrderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldCreateSalesOrderAndCalculateTotalAmount() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-001", new BigDecimal("50.00"), 10);

        mockMvc.perform(post("/api/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": 3
                                    }
                                  ]
                                }
                                """.formatted(customer.getId(), product.getId())))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/sales-orders/")))
                .andExpect(jsonPath("$.status").value("CRIADO"))
                .andExpect(jsonPath("$.items[0].unitPrice").value(50.00))
                .andExpect(jsonPath("$.items[0].subtotal").value(150.00))
                .andExpect(jsonPath("$.totalAmount").value(150.00));
    }

    @Test
    void shouldConfirmSalesOrderAndDecreaseStock() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-002", new BigDecimal("40.00"), 10);
        String location = createOrder(customer.getId(), product.getId(), 4);

        mockMvc.perform(patch(location + "/confirm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMADO"));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(6);
        assertThat(stockMovementRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldNotConfirmSalesOrderWhenStockIsInsufficient() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-003", new BigDecimal("40.00"), 2);
        String location = createOrder(customer.getId(), product.getId(), 4);

        mockMvc.perform(patch(location + "/confirm"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Nao ha estoque suficiente para realizar a saida."));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(2);
        assertThat(stockMovementRepository.count()).isZero();
    }

    @Test
    void shouldCancelConfirmedSalesOrderAndReturnStock() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-004", new BigDecimal("25.00"), 10);
        String location = createOrder(customer.getId(), product.getId(), 4);

        mockMvc.perform(patch(location + "/confirm"))
                .andExpect(status().isOk());

        mockMvc.perform(patch(location + "/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADO"));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStockQuantity()).isEqualTo(10);
        assertThat(stockMovementRepository.count()).isEqualTo(2);
    }

    @Test
    void shouldNotConfirmCanceledSalesOrder() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-005", new BigDecimal("30.00"), 10);
        String location = createOrder(customer.getId(), product.getId(), 1);

        mockMvc.perform(patch(location + "/cancel"))
                .andExpect(status().isOk());

        mockMvc.perform(patch(location + "/confirm"))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Pedido cancelado nao pode ser confirmado."));
    }

    @Test
    void shouldNotCreateSalesOrderForInactiveCustomer() throws Exception {
        Customer customer = saveCustomer();
        customer.deactivate();
        customerRepository.save(customer);
        Product product = saveProduct("ORDER-006", new BigDecimal("30.00"), 10);

        mockMvc.perform(post("/api/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """.formatted(customer.getId(), product.getId())))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Cliente inativo nao pode realizar pedido."));
    }

    @Test
    void shouldNotCreateSalesOrderWithInactiveProduct() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("ORDER-007", new BigDecimal("30.00"), 10);
        product.deactivate();
        productRepository.save(product);

        mockMvc.perform(post("/api/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": 1
                                    }
                                  ]
                                }
                                """.formatted(customer.getId(), product.getId())))
                .andExpect(status().isUnprocessableContent())
                .andExpect(jsonPath("$.message").value("Produto inativo nao pode ser vendido."));
    }

    @Test
    void shouldReturnBadRequestWhenOrderHasNoItems() throws Exception {
        Customer customer = saveCustomer();

        mockMvc.perform(post("/api/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "items": []
                                }
                                """.formatted(customer.getId())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Existem campos invalidos na requisicao."));
    }

    private String createOrder(Long customerId, Long productId, Integer quantity) throws Exception {
        return mockMvc.perform(post("/api/sales-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "customerId": %d,
                                  "items": [
                                    {
                                      "productId": %d,
                                      "quantity": %d
                                    }
                                  ]
                                }
                                """.formatted(customerId, productId, quantity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getHeader("Location");
    }

    private Customer saveCustomer() {
        return customerRepository.save(new Customer(
                "Cliente Pedido",
                "12345678900",
                "cliente.pedido@email.com",
                "11999999999"
        ));
    }

    private Product saveProduct(String sku, BigDecimal price, Integer stockQuantity) {
        return productRepository.save(new Product(
                "Produto Pedido",
                "Produto usado nos testes de pedido.",
                sku,
                price,
                stockQuantity,
                2
        ));
    }
}
