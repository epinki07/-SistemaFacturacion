package com.ramirez.sistemafacturacion.repository;

import com.ramirez.sistemafacturacion.model.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Integer> {
}
