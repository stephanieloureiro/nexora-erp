package com.nexora.erp.customer.service;

import com.nexora.erp.audit.service.AuditService;
import com.nexora.erp.common.exception.DuplicateResourceException;
import com.nexora.erp.customer.dto.CustomerCreateRequest;
import com.nexora.erp.customer.dto.CustomerResponse;
import com.nexora.erp.customer.entity.Customer;
import com.nexora.erp.customer.mapper.CustomerMapper;
import com.nexora.erp.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
    private final CustomerMapper customerMapper = new CustomerMapper();
    private final AuditService auditService = mock(AuditService.class);
    private final CustomerService customerService = new CustomerService(customerRepository, customerMapper, auditService);

    @Test
    void shouldCreateCustomerWhenDocumentDoesNotExist() {
        CustomerCreateRequest request = createRequest("Ana Silva", "123.456.789-00", "ana@email.com", "11999999999");

        when(customerRepository.existsByDocument("12345678900")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            ReflectionTestUtils.setField(customer, "id", 1L);
            customer.prePersist();
            return customer;
        });

        CustomerResponse response = customerService.create(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getDocument()).isEqualTo("12345678900");
        assertThat(response.getActive()).isTrue();
        verify(customerRepository).save(any(Customer.class));
        verify(auditService).record("CUSTOMER_CREATED", "Customer", 1L, "Cliente cadastrado.");
    }

    @Test
    void shouldNotCreateCustomerWhenDocumentAlreadyExists() {
        CustomerCreateRequest request = createRequest("Ana Silva", "123.456.789-00", "ana@email.com", "11999999999");

        when(customerRepository.existsByDocument("12345678900")).thenReturn(true);

        assertThatThrownBy(() -> customerService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Ja existe um cliente cadastrado com este CPF ou CNPJ.");

        verify(customerRepository, never()).save(any(Customer.class));
        verify(auditService, never()).record(any(), any(), any(), any());
    }

    private CustomerCreateRequest createRequest(String name, String document, String email, String phone) {
        CustomerCreateRequest request = new CustomerCreateRequest();
        request.setName(name);
        request.setDocument(document);
        request.setEmail(email);
        request.setPhone(phone);
        return request;
    }
}
