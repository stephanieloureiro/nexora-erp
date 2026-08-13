package com.nexora.erp.customer.dto;

import java.time.LocalDate;

public class CustomerResponse {

    private Long id;
    private String name;
    private String document;
    private String email;
    private String phone;
    private LocalDate registrationDate;
    private Boolean active;

    public CustomerResponse(Long id, String name, String document, String email, String phone,
                            LocalDate registrationDate, Boolean active) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.email = email;
        this.phone = phone;
        this.registrationDate = registrationDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocument() {
        return document;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public Boolean getActive() {
        return active;
    }
}
