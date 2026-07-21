package com.ramirez.sistemafacturacion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

@Entity
@Table(name = "impuestos_detalle_factura")
public class ImpuestoDetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El tipo de impuesto es obligatorio")
    @Pattern(regexp = "TRASLADO|RETENCION", message = "El tipo debe ser TRASLADO o RETENCION")
    @Column(name = "tipo", nullable = false, length = 10)
    private String tipo;

    @NotNull(message = "La base del impuesto es obligatoria")
    @DecimalMin(value = "0.000001", message = "La base del impuesto debe ser mayor que cero")
    @Column(name = "base", nullable = false, precision = 12, scale = 6)
    private BigDecimal base;

    @NotBlank(message = "La clave del impuesto es obligatoria")
    @Pattern(regexp = "001|002|003", message = "El impuesto debe ser 001 ISR, 002 IVA o 003 IEPS")
    @Column(name = "impuesto", nullable = false, length = 3)
    private String impuesto;

    @NotBlank(message = "El tipo factor es obligatorio")
    @Pattern(regexp = "Tasa|Cuota|Exento", message = "El tipo factor debe ser Tasa, Cuota o Exento")
    @Column(name = "tipo_factor", nullable = false, length = 10)
    private String tipoFactor;

    @PositiveOrZero(message = "La tasa o cuota no puede ser negativa")
    @Column(name = "tasa_o_cuota", precision = 8, scale = 6)
    private BigDecimal tasaOCuota;

    @PositiveOrZero(message = "El importe del impuesto no puede ser negativo")
    @Column(name = "importe", precision = 12, scale = 2)
    private BigDecimal importe;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_factura_id", nullable = false)
    private DetalleFactura detalleFactura;

    public ImpuestoDetalleFactura() {
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public DetalleFactura getDetalleFactura() {
        return detalleFactura;
    }

    public void setDetalleFactura(DetalleFactura detalleFactura) {
        this.detalleFactura = detalleFactura;
    }
}
