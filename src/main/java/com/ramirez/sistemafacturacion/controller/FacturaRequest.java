package com.ramirez.sistemafacturacion.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Data required to create an invoice")
public class FacturaRequest {

    @NotBlank(message = "El folio es obligatorio")
    @Schema(description = "Invoice folio", example = "FAC-003")
    private String folio;

    @Schema(description = "Registered client ID", example = "1")
    private Integer clienteId;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Schema(description = "Client name shown on the invoice", example = "Juan Perez")
    private String cliente;

    @NotNull(message = "La fecha es obligatoria")
    @Schema(description = "Invoice date", example = "2026-07-27")
    private LocalDate fecha;

    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Schema(description = "Invoice subtotal", example = "1500.00")
    private BigDecimal subtotal;

    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @Schema(description = "Invoice total", example = "1740.00")
    private BigDecimal total;

    @PositiveOrZero(message = "El total de impuestos trasladados no puede ser negativo")
    @Schema(description = "Total transferred taxes", example = "240.00")
    private BigDecimal totalImpuestosTrasladados;

    @PositiveOrZero(message = "El total de impuestos retenidos no puede ser negativo")
    @Schema(description = "Total withheld taxes", example = "0.00")
    private BigDecimal totalImpuestosRetenidos;

    @Valid
    @Schema(description = "Invoice details or line items")
    private List<DetalleFacturaRequest> detalles = new ArrayList<>();

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalImpuestosTrasladados() {
        return totalImpuestosTrasladados;
    }

    public void setTotalImpuestosTrasladados(BigDecimal totalImpuestosTrasladados) {
        this.totalImpuestosTrasladados = totalImpuestosTrasladados;
    }

    public BigDecimal getTotalImpuestosRetenidos() {
        return totalImpuestosRetenidos;
    }

    public void setTotalImpuestosRetenidos(BigDecimal totalImpuestosRetenidos) {
        this.totalImpuestosRetenidos = totalImpuestosRetenidos;
    }

    public List<DetalleFacturaRequest> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFacturaRequest> detalles) {
        this.detalles = detalles;
    }
}
