package com.ramirez.sistemafacturacion.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Schema(description = "Tax applied to an invoice line item")
public class ImpuestoDetalleFacturaRequest {

    @NotBlank(message = "El tipo de impuesto es obligatorio")
    @Pattern(regexp = "TRASLADO|RETENCION", message = "El tipo debe ser TRASLADO o RETENCION")
    @Schema(description = "Tax type", example = "TRASLADO")
    private String tipo;

    @NotNull(message = "La base del impuesto es obligatoria")
    @DecimalMin(value = "0.000001", message = "La base del impuesto debe ser mayor que cero")
    @Schema(description = "Tax base amount", example = "1000.000000")
    private BigDecimal base;

    @NotBlank(message = "La clave del impuesto es obligatoria")
    @Pattern(regexp = "001|002|003", message = "El impuesto debe ser 001 ISR, 002 IVA o 003 IEPS")
    @Schema(description = "SAT tax code", example = "002")
    private String impuesto;

    @NotBlank(message = "El tipo factor es obligatorio")
    @Pattern(regexp = "Tasa|Cuota|Exento", message = "El tipo factor debe ser Tasa, Cuota o Exento")
    @Schema(description = "Tax factor type", example = "Tasa")
    private String tipoFactor;

    @PositiveOrZero(message = "La tasa o cuota no puede ser negativa")
    @Schema(description = "Tax rate or fee", example = "0.160000")
    private BigDecimal tasaOCuota;

    @PositiveOrZero(message = "El importe del impuesto no puede ser negativo")
    @Schema(description = "Tax amount", example = "160.00")
    private BigDecimal importe;

    @Schema(hidden = true)
    @AssertTrue(message = "La tasa o cuota y el importe son obligatorios cuando el tipo factor es Tasa o Cuota; para Exento deben quedar vacios")
    public boolean isTasaEImporteValidos() {
        if (tipoFactor == null) {
            return true;
        }
        if ("Exento".equals(tipoFactor)) {
            return tasaOCuota == null && importe == null;
        }
        return tasaOCuota != null && importe != null;
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
