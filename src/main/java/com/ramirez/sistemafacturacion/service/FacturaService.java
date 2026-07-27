package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.mapper.FacturaMapper;
import com.ramirez.sistemafacturacion.model.Cliente;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.repository.ClienteRepository;
import com.ramirez.sistemafacturacion.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final FacturaMapper facturaMapper;

    public FacturaService(FacturaRepository facturaRepository, ClienteRepository clienteRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.facturaMapper = facturaMapper;
    }

    @Transactional(readOnly = true)
    public List<FacturaDomain> findAll() {
        return StreamSupport.stream(facturaRepository.findAll().spliterator(), false)
                .map(facturaMapper::toDomain)
                .toList();
    }

    @Transactional(readOnly = true)
    public FacturaDomain findById(Integer id) {
        return getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Factura no encontrada"));
    }

    @Transactional(readOnly = true)
    public Optional<FacturaDomain> getById(Integer id) {
        return facturaRepository.findById(id)
                .map(facturaMapper::toDomain);
    }

    @Transactional(readOnly = true)
    public Optional<List<FacturaDomain>> findByClienteId(Integer clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            return Optional.empty();
        }
        List<FacturaDomain> facturas = facturaRepository.findByClienteRegistroId(clienteId)
                .stream()
                .map(facturaMapper::toDomain)
                .toList();
        return Optional.of(facturas);
    }

    @Transactional
    public FacturaDomain save(FacturaDomain facturaDomain) {
        if (facturaDomain.getDetalles() == null || facturaDomain.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("La factura debe incluir al menos un detalle");
        }

        Factura factura = facturaMapper.toEntity(facturaDomain);
        if (facturaDomain.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(facturaDomain.getClienteId())
                    .orElseThrow(() -> new IllegalArgumentException("La factura contiene un cliente desconocido"));
            factura.setClienteRegistro(cliente);
        }

        return facturaMapper.toDomain(facturaRepository.save(factura));
    }

    @Transactional
    public boolean delete(Integer id) {
        if (!facturaRepository.existsById(id)) {
            return false;
        }
        facturaRepository.deleteById(id);
        return true;
    }
}
