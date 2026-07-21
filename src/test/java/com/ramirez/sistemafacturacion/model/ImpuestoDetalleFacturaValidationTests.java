package com.ramirez.sistemafacturacion.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ImpuestoDetalleFacturaValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void acceptsValidVatTransfer() {
        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(new BigDecimal("1000.000000"));
        impuesto.setImpuesto("002");
        impuesto.setTipoFactor("Tasa");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(new BigDecimal("160.00"));

        Set<ConstraintViolation<ImpuestoDetalleFactura>> violations = validator.validate(impuesto);

        assertThat(violations).isEmpty();
    }

    @Test
    void rejectsExemptTaxWithRateAndAmount() {
        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(new BigDecimal("1000.000000"));
        impuesto.setImpuesto("002");
        impuesto.setTipoFactor("Exento");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(new BigDecimal("160.00"));

        Set<ConstraintViolation<ImpuestoDetalleFactura>> violations = validator.validate(impuesto);

        assertThat(violations).anyMatch(violation -> "tasaEImporteValidos".equals(violation.getPropertyPath().toString()));
    }

    @Test
    void rejectsInvalidSatTaxCode() {
        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(new BigDecimal("1000.000000"));
        impuesto.setImpuesto("999");
        impuesto.setTipoFactor("Tasa");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(new BigDecimal("160.00"));

        Set<ConstraintViolation<ImpuestoDetalleFactura>> violations = validator.validate(impuesto);

        assertThat(violations).anyMatch(violation -> "impuesto".equals(violation.getPropertyPath().toString()));
    }
}
