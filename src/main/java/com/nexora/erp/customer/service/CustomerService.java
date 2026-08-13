package com.nexora.erp.customer.service;

import com.nexora.erp.common.exception.DuplicateResourceException;
import com.nexora.erp.common.exception.ResourceNotFoundException;
import com.nexora.erp.customer.dto.CustomerCreateRequest;
import com.nexora.erp.customer.dto.CustomerResponse;
import com.nexora.erp.customer.dto.CustomerUpdateRequest;
import com.nexora.erp.customer.entity.Customer;
import com.nexora.erp.customer.mapper.CustomerMapper;
import com.nexora.erp.customer.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerResponse create(CustomerCreateRequest request) {
        String normalizedDocument = normalizeDocument(request.getDocument());

        if (customerRepository.existsByDocument(normalizedDocument)) {
            throw new DuplicateResourceException("Ja existe um cliente cadastrado com este CPF ou CNPJ.");
        }

        request.setDocument(normalizedDocument);
        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        Customer customer = findCustomerById(id);
        return customerMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<CustomerResponse> searchByName(String name, Pageable pageable) {
        return customerRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(customerMapper::toResponse);
    }

    @Transactional
    public CustomerResponse update(Long id, CustomerUpdateRequest request) {
        Customer customer = findCustomerById(id);
        customer.update(request.getName(), request.getEmail(), request.getPhone());
        return customerMapper.toResponse(customer);
    }

    @Transactional
    public CustomerResponse deactivate(Long id) {
        Customer customer = findCustomerById(id);
        customer.deactivate();
        return customerMapper.toResponse(customer);
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente nao encontrado."));
    }

    private String normalizeDocument(String document) {
        return document.replaceAll("\\D", "");
    }
}
