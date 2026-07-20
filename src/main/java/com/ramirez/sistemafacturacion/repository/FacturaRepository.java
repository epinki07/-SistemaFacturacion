package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.Factura;
import org.springframework.data.repository.CrudRepository;

public interface FacturaRepository extends CrudRepository<Factura, Integer> {
}
