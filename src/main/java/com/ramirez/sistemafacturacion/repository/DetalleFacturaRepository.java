package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.DetalleFactura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacturaRepository extends CrudRepository<DetalleFactura, Integer> {
}
