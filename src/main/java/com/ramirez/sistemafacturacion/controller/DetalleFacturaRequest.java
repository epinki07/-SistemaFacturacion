package com.ramirez.sistemafacturacion.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Invoice line item")
public class DetalleFacturaRequest {

    @NotBlank(message = "La descripcion es obligatoria")
    @Schema(description = "Line item description", example = "Consulting service")
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Schema(description = "Invoiced quantity", example = "2")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    @Schema(description = "Unit price", example = "500.00")
    private BigDecimal precioUnitario;

    @NotNull(message = "El importe es obligatorio")
    @PositiveOrZero(message = "El importe no puede ser negativo")
    @Schema(description = "Line item amount", example = "1000.00")
    private BigDecimal importe;

    @NotBlank(message = "El objeto de impuesto es obligatorio")
    @Pattern(regexp = "01|02|03", message = "El objeto de impuesto debe ser 01, 02 o 03")
    @Schema(description = "SAT tax object code", example = "02")
    private String objetoImpuesto;

    @Valid
    @Schema(description = "Line item taxes")
    private List<ImpuestoDetalleFacturaRequest> impuestos = new ArrayList<>();

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public String getObjetoImpuesto() {
        return objetoImpuesto;
    }

    public void setObjetoImpuesto(String objetoImpuesto) {
        this.objetoImpuesto = objetoImpuesto;
    }

    public List<ImpuestoDetalleFacturaRequest> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoDetalleFacturaRequest> impuestos) {
        this.impuestos = impuestos;
    }
}
