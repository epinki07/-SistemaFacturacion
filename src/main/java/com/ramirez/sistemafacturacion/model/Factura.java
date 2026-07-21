package com.ramirez.sistemafacturacion.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El folio es obligatorio")
    @Column(name = "folio", nullable = false, unique = true, length = 50)
    private String folio;

    @NotBlank(message = "El cliente es obligatorio")
    @Column(name = "cliente", nullable = false, length = 150)
    private String cliente;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @NotNull(message = "El subtotal es obligatorio")
    @PositiveOrZero(message = "El subtotal no puede ser negativo")
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @NotNull(message = "El total es obligatorio")
    @PositiveOrZero(message = "El total no puede ser negativo")
    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @PositiveOrZero(message = "El total de impuestos trasladados no puede ser negativo")
    @Column(name = "total_impuestos_trasladados", precision = 12, scale = 2)
    private BigDecimal totalImpuestosTrasladados;

    @PositiveOrZero(message = "El total de impuestos retenidos no puede ser negativo")
    @Column(name = "total_impuestos_retenidos", precision = 12, scale = 2)
    private BigDecimal totalImpuestosRetenidos;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid DetalleFactura> detalles = new ArrayList<>();

    public Factura() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
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

    public List<@Valid DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<@Valid DetalleFactura> detalles) {
        this.detalles.clear();
        if (detalles != null) {
            detalles.forEach(this::addDetalle);
        }
    }

    public void addDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        detalle.setFactura(this);
    }

    public void removeDetalle(DetalleFactura detalle) {
        detalles.remove(detalle);
        detalle.setFactura(null);
    }
}
