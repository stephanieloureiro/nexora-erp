package com.nexora.erp.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CustomerCreateRequest {

    @NotBlank(message = "O nome do cliente e obrigatorio.")
    @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
    private String name;

    @NotBlank(message = "O CPF ou CNPJ e obrigatorio.")
    @Size(max = 20, message = "O CPF ou CNPJ deve ter no maximo 20 caracteres.")
    private String document;

    @NotBlank(message = "O e-mail e obrigatorio.")
    @Email(message = "O e-mail informado e invalido.")
    @Size(max = 150, message = "O e-mail deve ter no maximo 150 caracteres.")
    private String email;

    @Size(max = 30, message = "O telefone deve ter no maximo 30 caracteres.")
    private String phone;

    public CustomerCreateRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
