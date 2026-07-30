package com.ramirez.sistemafacturacion.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Partial tax update for an invoice line item")
public class ImpuestoDetalleFacturaPatchRequest {

    @Pattern(regexp = "TRASLADO|RETENCION", message = "El tipo debe ser TRASLADO o RETENCION")
    @Schema(description = "Tax type", example = "TRASLADO")
    private String tipo;

    @DecimalMin(value = "0.000001", message = "La base del impuesto debe ser mayor que cero")
    @Schema(description = "Tax base amount", example = "1000.000000")
    private BigDecimal base;

    @Pattern(regexp = "001|002|003", message = "El impuesto debe ser 001 ISR, 002 IVA o 003 IEPS")
    @Schema(description = "SAT tax code", example = "002")
    private String impuesto;

    @Pattern(regexp = "Tasa|Cuota|Exento", message = "El tipo factor debe ser Tasa, Cuota o Exento")
    @Schema(description = "Tax factor type", example = "Tasa")
    private String tipoFactor;

    @PositiveOrZero(message = "La tasa o cuota no puede ser negativa")
    @Schema(description = "Tax rate or fee", example = "0.080000")
    private BigDecimal tasaOCuota;

    @PositiveOrZero(message = "El importe del impuesto no puede ser negativo")
    @Schema(description = "Tax amount", example = "80.00")
    private BigDecimal importe;

    @Schema(hidden = true)
    @AssertTrue(message = "Para Exento, tasaOCuota e importe deben quedar vacios")
    public boolean isExentoValido() {
        return !"Exento".equals(tipoFactor) || tasaOCuota == null && importe == null;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    public String getTipoFactor() {
        return tipoFactor;
    }

    public void setTipoFactor(String tipoFactor) {
        this.tipoFactor = tipoFactor;
    }

    public BigDecimal getTasaOCuota() {
        return tasaOCuota;
    }

    public void setTasaOCuota(BigDecimal tasaOCuota) {
        this.tasaOCuota = tasaOCuota;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
}
