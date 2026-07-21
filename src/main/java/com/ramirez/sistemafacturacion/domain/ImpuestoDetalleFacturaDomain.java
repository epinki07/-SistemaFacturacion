package com.ramirez.sistemafacturacion.domain;

import java.math.BigDecimal;

public class ImpuestoDetalleFacturaDomain {

    private Integer id;
    private String tipo;
    private BigDecimal base;
    private String impuesto;
    private String tipoFactor;
    private BigDecimal tasaOCuota;
    private BigDecimal importe;
    private Integer detalleFacturaId;

    public ImpuestoDetalleFacturaDomain() {
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

    public Integer getDetalleFacturaId() {
        return detalleFacturaId;
    }

    public void setDetalleFacturaId(Integer detalleFacturaId) {
        this.detalleFacturaId = detalleFacturaId;
    }
}
