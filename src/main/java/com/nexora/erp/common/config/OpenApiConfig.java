package com.nexora.erp.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI nexoraOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("NEXORA ERP - Sistema de Gestao Comercial")
                        .description("API REST para gerenciamento de clientes, produtos, estoque e pedidos de venda.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Projeto de portfolio")
                                .url("https://github.com/")));
    }
}
