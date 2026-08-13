package com.nexora.erp.product.mapper;

import com.nexora.erp.product.dto.ProductCreateRequest;
import com.nexora.erp.product.dto.ProductResponse;
import com.nexora.erp.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateRequest request) {
        return new Product(
                request.getName(),
                request.getDescription(),
                request.getSku(),
                request.getPrice(),
                request.getStockQuantity(),
                request.getMinimumStock()
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSku(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getMinimumStock(),
                product.getActive(),
                product.isLowStock()
        );
    }
}
