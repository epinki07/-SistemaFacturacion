package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.model.Cliente;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.Factura;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = DetalleFacturaMapper.class)
public interface FacturaMapper {

    @Mapping(source = "clienteRegistro.id", target = "clienteId")
    FacturaDomain toDomain(Factura factura);

    @Mapping(source = "clienteId", target = "clienteRegistro")
    Factura toEntity(FacturaDomain facturaDomain);

    List<FacturaDomain> toDomainList(List<Factura> facturas);

    List<Factura> toEntityList(List<FacturaDomain> facturas);

    default Cliente mapCliente(Integer clienteId) {
        if (clienteId == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        return cliente;
    }

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
