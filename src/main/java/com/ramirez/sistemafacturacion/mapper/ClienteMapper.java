package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.ClienteDomain;
import com.ramirez.sistemafacturacion.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDomain toDomain(Cliente cliente);

    @Mapping(target = "facturas", ignore = true)
    Cliente toEntity(ClienteDomain clienteDomain);

    List<ClienteDomain> toDomainList(List<Cliente> clientes);

    List<Cliente> toEntityList(List<ClienteDomain> clientes);
}
