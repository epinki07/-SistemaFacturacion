package com.ramirez.sistemafacturacion.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DomainModelTests {

    @Test
    void domainModelsDoNotUseJpaAnnotations() {
        List<Class<?>> domainTypes = List.of(
                FacturaDomain.class,
                DetalleFacturaDomain.class,
                ImpuestoDetalleFacturaDomain.class
        );

        for (Class<?> domainType : domainTypes) {
            assertThat(domainType.isAnnotationPresent(Entity.class)).isFalse();
            assertThat(domainType.isAnnotationPresent(Table.class)).isFalse();
            for (Field field : domainType.getDeclaredFields()) {
                assertThat(field.isAnnotationPresent(Id.class)).isFalse();
            }
        }
    }
}
