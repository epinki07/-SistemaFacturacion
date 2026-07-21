package com.ramirez.sistemafacturacion.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDomain {

    private Integer id;
    private String descripcion;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal importe;
    private String objetoImpuesto;
    private Integer facturaId;
    private List<ImpuestoDetalleFacturaDomain> impuestos = new ArrayList<>();

    public DetalleFacturaDomain() {
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

    public Integer getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(Integer facturaId) {
        this.facturaId = facturaId;
    }

    public List<ImpuestoDetalleFacturaDomain> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoDetalleFacturaDomain> impuestos) {
        this.impuestos = impuestos;
    }
}
