package com.nexora.erp.customer.repository;

import com.nexora.erp.customer.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByDocument(String document);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
