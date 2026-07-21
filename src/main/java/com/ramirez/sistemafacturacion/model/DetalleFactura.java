package com.ramirez.sistemafacturacion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "detalles_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La descripcion es obligatoria")
    @Column(name = "descripcion", nullable = false, length = 250)
    private String descripcion;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @PositiveOrZero(message = "El precio unitario no puede ser negativo")
    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull(message = "El importe es obligatorio")
    @PositiveOrZero(message = "El importe no puede ser negativo")
    @Column(name = "importe", nullable = false, precision = 12, scale = 2)
    private BigDecimal importe;

    @NotBlank(message = "El objeto de impuesto es obligatorio")
    @Pattern(regexp = "01|02|03", message = "El objeto de impuesto debe ser 01, 02 o 03")
    @Column(name = "objeto_impuesto", nullable = false, length = 2)
    private String objetoImpuesto;

    @OneToMany(mappedBy = "detalleFactura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@Valid ImpuestoDetalleFactura> impuestos = new ArrayList<>();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    public DetalleFactura() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<@Valid ImpuestoDetalleFactura> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<@Valid ImpuestoDetalleFactura> impuestos) {
        this.impuestos.clear();
        if (impuestos != null) {
            impuestos.forEach(this::addImpuesto);
        }
    }

    public void addImpuesto(ImpuestoDetalleFactura impuesto) {
        impuestos.add(impuesto);
        impuesto.setDetalleFactura(this);
    }

    public void removeImpuesto(ImpuestoDetalleFactura impuesto) {
        impuestos.remove(impuesto);
        impuesto.setDetalleFactura(null);
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }
}
