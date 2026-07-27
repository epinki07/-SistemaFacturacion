package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.mapper.ImpuestoDetalleFacturaMapper;
import com.ramirez.sistemafacturacion.repository.DetalleFacturaRepository;
import com.ramirez.sistemafacturacion.repository.ImpuestoDetalleFacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class ImpuestoDetalleFacturaService {

    private final ImpuestoDetalleFacturaRepository impuestoDetalleFacturaRepository;
    private final DetalleFacturaRepository detalleFacturaRepository;
    private final ImpuestoDetalleFacturaMapper impuestoDetalleFacturaMapper;

    public ImpuestoDetalleFacturaService(ImpuestoDetalleFacturaRepository impuestoDetalleFacturaRepository,
                                         DetalleFacturaRepository detalleFacturaRepository,
                                         ImpuestoDetalleFacturaMapper impuestoDetalleFacturaMapper) {
        this.impuestoDetalleFacturaRepository = impuestoDetalleFacturaRepository;
        this.detalleFacturaRepository = detalleFacturaRepository;
        this.impuestoDetalleFacturaMapper = impuestoDetalleFacturaMapper;
    }

    @Transactional(readOnly = true)
    public List<ImpuestoDetalleFacturaDomain> findAll() {
        return StreamSupport.stream(impuestoDetalleFacturaRepository.findAll().spliterator(), false)
                .map(impuestoDetalleFacturaMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ImpuestoDetalleFacturaDomain> getById(Integer id) {
        return impuestoDetalleFacturaRepository.findById(id)
                .map(impuestoDetalleFacturaMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Optional<List<ImpuestoDetalleFacturaDomain>> findByDetalleId(Integer detalleId) {
        if (!detalleFacturaRepository.existsById(detalleId)) {
            return Optional.empty();
        }
        List<ImpuestoDetalleFacturaDomain> impuestos = impuestoDetalleFacturaRepository.findByDetalleFacturaId(detalleId)
                .stream()
                .map(impuestoDetalleFacturaMapper::toDomain)
                .toList();
        return Optional.of(impuestos);
    }
}
