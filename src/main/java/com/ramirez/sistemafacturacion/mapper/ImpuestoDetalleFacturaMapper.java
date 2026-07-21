package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImpuestoDetalleFacturaMapper {

    @Mapping(source = "detalleFactura.id", target = "detalleFacturaId")
    ImpuestoDetalleFacturaDomain toDomain(ImpuestoDetalleFactura impuestoDetalleFactura);

    @Mapping(source = "detalleFacturaId", target = "detalleFactura")
    ImpuestoDetalleFactura toEntity(ImpuestoDetalleFacturaDomain impuestoDetalleFacturaDomain);

    List<ImpuestoDetalleFacturaDomain> toDomainList(List<ImpuestoDetalleFactura> impuestosDetalleFactura);

    List<ImpuestoDetalleFactura> toEntityList(List<ImpuestoDetalleFacturaDomain> impuestosDetalleFactura);

    default DetalleFactura mapDetalleFactura(Integer detalleFacturaId) {
        if (detalleFacturaId == null) {
            return null;
        }
        DetalleFactura detalleFactura = new DetalleFactura();
        detalleFactura.setId(detalleFacturaId);
        return detalleFactura;
    }
}
