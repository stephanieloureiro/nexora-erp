package com.nexora.erp.customer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String document;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(nullable = false)
    private Boolean active;

    protected Customer() {
    }

    public Customer(String name, String document, String email, String phone) {
        this.name = name;
        this.document = document;
        this.email = email;
        this.phone = phone;
    }

    @PrePersist
    public void prePersist() {
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (active == null) {
            active = true;
        }
    }

    public void update(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void deactivate() {
        this.active = false;
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
