package com.ramirez.sistemafacturacion.service;

import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import com.ramirez.sistemafacturacion.repository.FacturaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class FacturaServiceTests {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private FacturaRepository facturaRepository;

    @Test
    void patchImpuestoUpdatesOnlySelectedTaxAndRecalculatesInvoiceTotals() {
        Factura factura = findFacturaByFolio("FAC-001");
        ImpuestoDetalleFactura impuesto = factura.getDetalles().get(0).getImpuestos().get(0);

        ImpuestoDetalleFacturaDomain patch = new ImpuestoDetalleFacturaDomain();
        patch.setTasaOCuota(new BigDecimal("0.080000"));
        patch.setImporte(new BigDecimal("80.00"));

        Optional<FacturaDomain> resultado = facturaService.patchImpuesto(factura.getId(), impuesto.getId(), patch);

        assertThat(resultado).isPresent();
        FacturaDomain facturaActualizada = resultado.get();
        assertThat(facturaActualizada.getTotalImpuestosTrasladados()).isEqualByComparingTo("160.00");
        assertThat(facturaActualizada.getTotalImpuestosRetenidos()).isEqualByComparingTo("0.00");
        assertThat(facturaActualizada.getTotal()).isEqualByComparingTo("1660.00");
        assertThat(facturaActualizada.getDetalles().get(0).getImpuestos().get(0).getTasaOCuota())
                .isEqualByComparingTo("0.080000");
        assertThat(facturaActualizada.getDetalles().get(0).getImpuestos().get(0).getImporte())
                .isEqualByComparingTo("80.00");
    }

    @Test
    void patchImpuestoReturnsEmptyWhenTaxDoesNotBelongToInvoice() {
        Factura factura = findFacturaByFolio("FAC-001");
        Factura otraFactura = findFacturaByFolio("FAC-003");
        ImpuestoDetalleFactura impuestoDeOtraFactura = otraFactura.getDetalles().get(0).getImpuestos().get(0);

        ImpuestoDetalleFacturaDomain patch = new ImpuestoDetalleFacturaDomain();
        patch.setImporte(new BigDecimal("50.00"));

        Optional<FacturaDomain> resultado = facturaService.patchImpuesto(
                factura.getId(),
                impuestoDeOtraFactura.getId(),
                patch
        );

        assertThat(resultado).isEmpty();
    }

    @Test
    void patchImpuestoRejectsExentoWithTaxAmount() {
        Factura factura = findFacturaByFolio("FAC-001");
        ImpuestoDetalleFactura impuesto = factura.getDetalles().get(0).getImpuestos().get(0);

        ImpuestoDetalleFacturaDomain patch = new ImpuestoDetalleFacturaDomain();
        patch.setTipoFactor("Exento");
        patch.setImporte(new BigDecimal("80.00"));

        assertThatThrownBy(() -> facturaService.patchImpuesto(factura.getId(), impuesto.getId(), patch))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Exento");
    }

    private Factura findFacturaByFolio(String folio) {
        return StreamSupport.stream(facturaRepository.findAll().spliterator(), false)
                .filter(factura -> folio.equals(factura.getFolio()))
                .findFirst()
                .orElseThrow();
    }
}
