package com.nexora.erp.report.controller;

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

import static com.nexora.erp.support.SecurityMockMvc.employee;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerIntegrationTest {

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
    void shouldReturnSalesSummary() throws Exception {
        Customer customer = saveCustomer();
        Product product = saveProduct("REPORT-001", new BigDecimal("20.00"), 10, 2);

        String location = createOrder(customer.getId(), product.getId(), 2);

        mockMvc.perform(patch(location + "/confirm")
                        .with(employee()))
                .andExpect(status().isOk());

        createOrder(customer.getId(), product.getId(), 1);

        mockMvc.perform(get("/api/reports/sales-summary")
                        .with(employee()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdOrders").value(1))
                .andExpect(jsonPath("$.confirmedOrders").value(1))
                .andExpect(jsonPath("$.canceledOrders").value(0))
                .andExpect(jsonPath("$.confirmedRevenue").value(40.00));
    }

    @Test
    void shouldReturnTopProducts() throws Exception {
        Customer customer = saveCustomer();
        Product keyboard = saveProduct("REPORT-002", new BigDecimal("100.00"), 10, 2);
        Product mouse = saveProduct("REPORT-003", new BigDecimal("50.00"), 10, 2);

        String firstOrder = createOrder(customer.getId(), keyboard.getId(), 1);
        String secondOrder = createOrder(customer.getId(), mouse.getId(), 3);

        mockMvc.perform(patch(firstOrder + "/confirm").with(employee()))
                .andExpect(status().isOk());
        mockMvc.perform(patch(secondOrder + "/confirm").with(employee()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/reports/top-products")
                        .with(employee()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("REPORT-003"))
                .andExpect(jsonPath("$[0].quantitySold").value(3))
                .andExpect(jsonPath("$[0].revenue").value(150.00))
                .andExpect(jsonPath("$[1].sku").value("REPORT-002"));
    }

    @Test
    void shouldReturnStockSummary() throws Exception {
        saveProduct("REPORT-004", new BigDecimal("10.00"), 2, 5);
        saveProduct("REPORT-005", new BigDecimal("15.00"), 8, 3);

        mockMvc.perform(get("/api/reports/stock-summary")
                        .with(employee()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activeProducts").value(2))
                .andExpect(jsonPath("$.lowStockProducts").value(1))
                .andExpect(jsonPath("$.totalStockQuantity").value(10));
    }

    private String createOrder(Long customerId, Long productId, Integer quantity) throws Exception {
        return mockMvc.perform(post("/api/sales-orders")
                        .with(employee())
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
                "Cliente Relatorio",
                "55544433322",
                "relatorio@email.com",
                "11911112222"
        ));
    }

    private Product saveProduct(String sku, BigDecimal price, Integer stockQuantity, Integer minimumStock) {
        return productRepository.save(new Product(
                "Produto Relatorio",
                "Produto usado nos testes de relatorio.",
                sku,
                price,
                stockQuantity,
                minimumStock
        ));
    }
}
