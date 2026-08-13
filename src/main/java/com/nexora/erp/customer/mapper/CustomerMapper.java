package com.nexora.erp.customer.mapper;

import com.nexora.erp.customer.dto.CustomerCreateRequest;
import com.nexora.erp.customer.dto.CustomerResponse;
import com.nexora.erp.customer.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerCreateRequest request) {
        return new Customer(
                request.getName(),
                request.getDocument(),
                request.getEmail(),
                request.getPhone()
        );
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getDocument(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getRegistrationDate(),
                customer.getActive()
        );
    }
}
