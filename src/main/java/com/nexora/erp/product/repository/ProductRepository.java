package com.nexora.erp.product.repository;

import com.nexora.erp.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySku(String sku);

    Page<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku, Pageable pageable);

    @Query("select product from Product product where product.stockQuantity <= product.minimumStock")
    Page<Product> findLowStock(Pageable pageable);

    long countByActiveTrue();

    @Query("""
            select count(product) from Product product
            where product.active = true
            and product.stockQuantity <= product.minimumStock
            """)
    long countActiveLowStock();

    @Query("""
            select coalesce(sum(product.stockQuantity), 0)
            from Product product
            where product.active = true
            """)
    Long sumActiveStockQuantity();
}
