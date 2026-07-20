package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.DetalleFactura;
import org.springframework.data.repository.CrudRepository;

public interface DetalleFacturaRepository extends CrudRepository<DetalleFactura, Integer> {
}
