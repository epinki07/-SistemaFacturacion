package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.DetalleFacturaDomain;
import com.ramirez.sistemafacturacion.mapper.DetalleFacturaMapper;
import com.ramirez.sistemafacturacion.repository.DetalleFacturaRepository;
import com.ramirez.sistemafacturacion.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class DetalleFacturaService {

    private final DetalleFacturaRepository detalleFacturaRepository;
    private final FacturaRepository facturaRepository;
    private final DetalleFacturaMapper detalleFacturaMapper;

    public DetalleFacturaService(DetalleFacturaRepository detalleFacturaRepository,
                                 FacturaRepository facturaRepository,
                                 DetalleFacturaMapper detalleFacturaMapper) {
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.facturaRepository = facturaRepository;
        this.detalleFacturaMapper = detalleFacturaMapper;
    }

    @Transactional(readOnly = true)
    public List<DetalleFacturaDomain> findAll() {
        return StreamSupport.stream(detalleFacturaRepository.findAll().spliterator(), false)
                .map(detalleFacturaMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<DetalleFacturaDomain> getById(Integer id) {
        return detalleFacturaRepository.findById(id)
                .map(detalleFacturaMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Optional<List<DetalleFacturaDomain>> findByFacturaId(Integer facturaId) {
        if (!facturaRepository.existsById(facturaId)) {
            return Optional.empty();
        }
        List<DetalleFacturaDomain> detalles = detalleFacturaRepository.findByFacturaId(facturaId)
                .stream()
                .map(detalleFacturaMapper::toDomain)
                .toList();
        return Optional.of(detalles);
    }
}
