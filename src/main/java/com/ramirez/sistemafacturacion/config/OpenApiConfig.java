package com.ramirez.sistemafacturacion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sistemaFacturacionOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Billing System API")
                        .version("1.0")
                        .description("REST API to manage clients, invoices, invoice details, and taxes."));
    }
}
