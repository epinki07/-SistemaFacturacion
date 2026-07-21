package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.DetalleFacturaDomain;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = ImpuestoDetalleFacturaMapper.class)
public interface DetalleFacturaMapper {

    @Mapping(source = "factura.id", target = "facturaId")
    DetalleFacturaDomain toDomain(DetalleFactura detalleFactura);

    @Mapping(source = "facturaId", target = "factura")
    DetalleFactura toEntity(DetalleFacturaDomain detalleFacturaDomain);

    List<DetalleFacturaDomain> toDomainList(List<DetalleFactura> detallesFactura);

    List<DetalleFactura> toEntityList(List<DetalleFacturaDomain> detallesFactura);

    default Factura mapFactura(Integer facturaId) {
        if (facturaId == null) {
            return null;
        }
        Factura factura = new Factura();
        factura.setId(facturaId);
        return factura;
    }

    @AfterMapping
    default void linkImpuestos(@MappingTarget DetalleFactura detalleFactura) {
        if (detalleFactura.getImpuestos() == null) {
            return;
        }
        for (ImpuestoDetalleFactura impuesto : detalleFactura.getImpuestos()) {
            impuesto.setDetalleFactura(detalleFactura);
        }
    }
}
