package com.nexora.erp.product.service;

import com.nexora.erp.common.exception.DuplicateResourceException;
import com.nexora.erp.product.dto.ProductCreateRequest;
import com.nexora.erp.product.dto.ProductResponse;
import com.nexora.erp.product.entity.Product;
import com.nexora.erp.product.mapper.ProductMapper;
import com.nexora.erp.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductMapper productMapper = new ProductMapper();
    private final ProductService productService = new ProductService(productRepository, productMapper);

    @Test
    void shouldCreateProductWhenSkuDoesNotExist() {
        ProductCreateRequest request = createRequest("Mouse sem fio", "mouse-001");

        when(productRepository.existsBySku("MOUSE-001")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            ReflectionTestUtils.setField(product, "id", 1L);
            product.prePersist();
            return product;
        });

        ProductResponse response = productService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getSku()).isEqualTo("MOUSE-001");
        assertThat(response.getActive()).isTrue();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldNotCreateProductWhenSkuAlreadyExists() {
        ProductCreateRequest request = createRequest("Mouse sem fio", "mouse-001");

        when(productRepository.existsBySku("MOUSE-001")).thenReturn(true);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Ja existe um produto cadastrado com este SKU.");

        verify(productRepository, never()).save(any(Product.class));
    }

    private ProductCreateRequest createRequest(String name, String sku) {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName(name);
        request.setDescription("Produto para testes.");
        request.setSku(sku);
        request.setPrice(new BigDecimal("89.90"));
        request.setStockQuantity(5);
        request.setMinimumStock(2);
        return request;
    }
}
