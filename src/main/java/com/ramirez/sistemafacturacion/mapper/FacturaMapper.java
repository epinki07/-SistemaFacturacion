package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.Factura;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = DetalleFacturaMapper.class)
public interface FacturaMapper {

    FacturaDomain toDomain(Factura factura);

    Factura toEntity(FacturaDomain facturaDomain);

    List<FacturaDomain> toDomainList(List<Factura> facturas);

    List<Factura> toEntityList(List<FacturaDomain> facturas);

    @AfterMapping
    default void linkDetalles(@MappingTarget Factura factura) {
        if (factura.getDetalles() == null) {
            return;
        }
        for (DetalleFactura detalle : factura.getDetalles()) {
            detalle.setFactura(factura);
        }
    }
}
