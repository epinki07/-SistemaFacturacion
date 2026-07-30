package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.mapper.FacturaMapper;
import com.ramirez.sistemafacturacion.model.Cliente;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import com.ramirez.sistemafacturacion.repository.ClienteRepository;
import com.ramirez.sistemafacturacion.repository.FacturaRepository;
import com.ramirez.sistemafacturacion.repository.ImpuestoDetalleFacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ImpuestoDetalleFacturaRepository impuestoDetalleFacturaRepository;
    private final FacturaMapper facturaMapper;

    public FacturaService(FacturaRepository facturaRepository,
                          ClienteRepository clienteRepository,
                          ImpuestoDetalleFacturaRepository impuestoDetalleFacturaRepository,
                          FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.impuestoDetalleFacturaRepository = impuestoDetalleFacturaRepository;
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

    @Transactional
    public Optional<FacturaDomain> patchImpuesto(Integer facturaId,
                                                 Integer impuestoId,
                                                 ImpuestoDetalleFacturaDomain impuestoPatch) {
        Optional<Factura> facturaOptional = facturaRepository.findById(facturaId);
        Optional<ImpuestoDetalleFactura> impuestoOptional = impuestoDetalleFacturaRepository.findById(impuestoId);

        if (facturaOptional.isEmpty() || impuestoOptional.isEmpty()) {
            return Optional.empty();
        }

        Factura factura = facturaOptional.get();
        ImpuestoDetalleFactura impuesto = impuestoOptional.get();
        if (!impuestoPerteneceAFactura(impuesto, facturaId)) {
            return Optional.empty();
        }

        aplicarPatchImpuesto(impuesto, impuestoPatch);
        validarImpuesto(impuesto);
        recalcularTotales(factura);
        return Optional.of(facturaMapper.toDomain(facturaRepository.save(factura)));
    }

    private boolean impuestoPerteneceAFactura(ImpuestoDetalleFactura impuesto, Integer facturaId) {
        return impuesto.getDetalleFactura() != null
                && impuesto.getDetalleFactura().getFactura() != null
                && facturaId.equals(impuesto.getDetalleFactura().getFactura().getId());
    }

    private void aplicarPatchImpuesto(ImpuestoDetalleFactura impuesto,
                                      ImpuestoDetalleFacturaDomain impuestoPatch) {
        if (impuestoPatch.getTipo() != null) {
            impuesto.setTipo(impuestoPatch.getTipo());
        }
        if (impuestoPatch.getBase() != null) {
            impuesto.setBase(impuestoPatch.getBase());
        }
        if (impuestoPatch.getImpuesto() != null) {
            impuesto.setImpuesto(impuestoPatch.getImpuesto());
        }
        if (impuestoPatch.getTipoFactor() != null) {
            impuesto.setTipoFactor(impuestoPatch.getTipoFactor());
            if ("Exento".equals(impuestoPatch.getTipoFactor())) {
                impuesto.setTasaOCuota(null);
                impuesto.setImporte(null);
            }
        }
        if (impuestoPatch.getTasaOCuota() != null) {
            impuesto.setTasaOCuota(impuestoPatch.getTasaOCuota());
        }
        if (impuestoPatch.getImporte() != null) {
            impuesto.setImporte(impuestoPatch.getImporte());
        }
    }

    private void validarImpuesto(ImpuestoDetalleFactura impuesto) {
        if ("Exento".equals(impuesto.getTipoFactor())) {
            if (impuesto.getTasaOCuota() != null || impuesto.getImporte() != null) {
                throw new IllegalArgumentException("Para Exento, tasaOCuota e importe deben quedar vacios");
            }
            return;
        }

        if (impuesto.getTasaOCuota() == null || impuesto.getImporte() == null) {
            throw new IllegalArgumentException("La tasa o cuota y el importe son obligatorios cuando el tipo factor es Tasa o Cuota");
        }
    }

    private void recalcularTotales(Factura factura) {
        BigDecimal trasladados = BigDecimal.ZERO;
        BigDecimal retenidos = BigDecimal.ZERO;

        for (var detalle : factura.getDetalles()) {
            for (var impuesto : detalle.getImpuestos()) {
                BigDecimal importe = impuesto.getImporte() == null ? BigDecimal.ZERO : impuesto.getImporte();
                if ("TRASLADO".equals(impuesto.getTipo())) {
                    trasladados = trasladados.add(importe);
                } else if ("RETENCION".equals(impuesto.getTipo())) {
                    retenidos = retenidos.add(importe);
                }
            }
        }

        factura.setTotalImpuestosTrasladados(trasladados.setScale(2, RoundingMode.HALF_UP));
        factura.setTotalImpuestosRetenidos(retenidos.setScale(2, RoundingMode.HALF_UP));
        factura.setTotal(factura.getSubtotal().add(trasladados).subtract(retenidos).setScale(2, RoundingMode.HALF_UP));
    }
}
