package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImpuestoDetalleFacturaRepository extends CrudRepository<ImpuestoDetalleFactura, Integer> {
}
