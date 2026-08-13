package com.nexora.erp.product.service;

import com.nexora.erp.common.exception.DuplicateResourceException;
import com.nexora.erp.common.exception.ResourceNotFoundException;
import com.nexora.erp.product.dto.ProductCreateRequest;
import com.nexora.erp.product.dto.ProductResponse;
import com.nexora.erp.product.dto.ProductUpdateRequest;
import com.nexora.erp.product.entity.Product;
import com.nexora.erp.product.mapper.ProductMapper;
import com.nexora.erp.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse create(ProductCreateRequest request) {
        String normalizedSku = normalizeSku(request.getSku());

        if (productRepository.existsBySku(normalizedSku)) {
            throw new DuplicateResourceException("Ja existe um produto cadastrado com este SKU.");
        }

        request.setSku(normalizedSku);
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = findProductById(id);
        return productMapper.toResponse(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> search(String term, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(term, term, pageable)
                .map(productMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findLowStock(Pageable pageable) {
        return productRepository.findLowStock(pageable)
                .map(productMapper::toResponse);
    }

    @Transactional
    public ProductResponse update(Long id, ProductUpdateRequest request) {
        Product product = findProductById(id);
        String normalizedSku = normalizeSku(request.getSku());

        if (!product.getSku().equals(normalizedSku) && productRepository.existsBySku(normalizedSku)) {
            throw new DuplicateResourceException("Ja existe um produto cadastrado com este SKU.");
        }

        product.update(
                request.getName(),
                request.getDescription(),
                normalizedSku,
                request.getPrice(),
                request.getStockQuantity(),
                request.getMinimumStock()
        );

        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse deactivate(Long id) {
        Product product = findProductById(id);
        product.deactivate();
        return productMapper.toResponse(product);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado."));
    }

    private String normalizeSku(String sku) {
        return sku.trim().toUpperCase();
    }
}
