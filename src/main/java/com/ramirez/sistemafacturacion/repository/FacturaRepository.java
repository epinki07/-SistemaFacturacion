package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.Factura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacturaRepository extends CrudRepository<Factura, Integer> {

    List<Factura> findByClienteRegistroId(Integer clienteId);
}
