package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.DetalleFactura;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleFacturaRepository extends CrudRepository<DetalleFactura, Integer> {

    List<DetalleFactura> findByFacturaId(Integer facturaId);
}
