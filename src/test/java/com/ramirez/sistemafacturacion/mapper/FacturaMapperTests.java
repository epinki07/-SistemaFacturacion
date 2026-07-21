package com.ramirez.sistemafacturacion.mapper;

import com.ramirez.sistemafacturacion.domain.DetalleFacturaDomain;
import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FacturaMapperTests {

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private DetalleFacturaMapper detalleFacturaMapper;

    @Autowired
    private ImpuestoDetalleFacturaMapper impuestoDetalleFacturaMapper;

    @Test
    void mapperBeansAreInjectedInSpringContext() {
        assertThat(facturaMapper).isNotNull();
        assertThat(detalleFacturaMapper).isNotNull();
        assertThat(impuestoDetalleFacturaMapper).isNotNull();
    }

    @Test
    void mapsInvoiceEntityToDomain() {
        Factura factura = buildFacturaEntity();

        FacturaDomain facturaDomain = facturaMapper.toDomain(factura);

        assertThat(facturaDomain.getId()).isEqualTo(1);
        assertThat(facturaDomain.getFolio()).isEqualTo("FAC-001");
        assertThat(facturaDomain.getDetalles()).hasSize(1);

        DetalleFacturaDomain detalleDomain = facturaDomain.getDetalles().get(0);
        assertThat(detalleDomain.getId()).isEqualTo(10);
        assertThat(detalleDomain.getFacturaId()).isEqualTo(1);
        assertThat(detalleDomain.getImpuestos()).hasSize(1);

        ImpuestoDetalleFacturaDomain impuestoDomain = detalleDomain.getImpuestos().get(0);
        assertThat(impuestoDomain.getId()).isEqualTo(100);
        assertThat(impuestoDomain.getDetalleFacturaId()).isEqualTo(10);
        assertThat(impuestoDomain.getImpuesto()).isEqualTo("002");
    }

    @Test
    void mapsInvoiceDomainToEntityAndLinksRelationships() {
        FacturaDomain facturaDomain = buildFacturaDomain();

        Factura factura = facturaMapper.toEntity(facturaDomain);

        assertThat(factura.getId()).isEqualTo(2);
        assertThat(factura.getDetalles()).hasSize(1);

        DetalleFactura detalle = factura.getDetalles().get(0);
        assertThat(detalle.getFactura()).isSameAs(factura);
        assertThat(detalle.getImpuestos()).hasSize(1);

        ImpuestoDetalleFactura impuesto = detalle.getImpuestos().get(0);
        assertThat(impuesto.getDetalleFactura()).isSameAs(detalle);
        assertThat(impuesto.getTipo()).isEqualTo("TRASLADO");
    }

    private Factura buildFacturaEntity() {
        Factura factura = new Factura();
        factura.setId(1);
        factura.setFolio("FAC-001");
        factura.setCliente("Juan Perez");
        factura.setFecha(LocalDate.of(2026, 7, 21));
        factura.setSubtotal(new BigDecimal("1000.00"));
        factura.setTotal(new BigDecimal("1160.00"));
        factura.setTotalImpuestosTrasladados(new BigDecimal("160.00"));
        factura.setTotalImpuestosRetenidos(BigDecimal.ZERO);

        DetalleFactura detalle = new DetalleFactura();
        detalle.setId(10);
        detalle.setDescripcion("Servicio de consultoria");
        detalle.setCantidad(1);
        detalle.setPrecioUnitario(new BigDecimal("1000.00"));
        detalle.setImporte(new BigDecimal("1000.00"));
        detalle.setObjetoImpuesto("02");

        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setId(100);
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(new BigDecimal("1000.000000"));
        impuesto.setImpuesto("002");
        impuesto.setTipoFactor("Tasa");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(new BigDecimal("160.00"));

        detalle.addImpuesto(impuesto);
        factura.addDetalle(detalle);

        return factura;
    }

    private FacturaDomain buildFacturaDomain() {
        FacturaDomain facturaDomain = new FacturaDomain();
        facturaDomain.setId(2);
        facturaDomain.setFolio("FAC-002");
        facturaDomain.setCliente("Maria Lopez");
        facturaDomain.setFecha(LocalDate.of(2026, 7, 22));
        facturaDomain.setSubtotal(new BigDecimal("500.00"));
        facturaDomain.setTotal(new BigDecimal("580.00"));
        facturaDomain.setTotalImpuestosTrasladados(new BigDecimal("80.00"));
        facturaDomain.setTotalImpuestosRetenidos(BigDecimal.ZERO);

        DetalleFacturaDomain detalleDomain = new DetalleFacturaDomain();
        detalleDomain.setId(20);
        detalleDomain.setDescripcion("Licencia de software");
        detalleDomain.setCantidad(1);
        detalleDomain.setPrecioUnitario(new BigDecimal("500.00"));
        detalleDomain.setImporte(new BigDecimal("500.00"));
        detalleDomain.setObjetoImpuesto("02");

        ImpuestoDetalleFacturaDomain impuestoDomain = new ImpuestoDetalleFacturaDomain();
        impuestoDomain.setId(200);
        impuestoDomain.setTipo("TRASLADO");
        impuestoDomain.setBase(new BigDecimal("500.000000"));
        impuestoDomain.setImpuesto("002");
        impuestoDomain.setTipoFactor("Tasa");
        impuestoDomain.setTasaOCuota(new BigDecimal("0.160000"));
        impuestoDomain.setImporte(new BigDecimal("80.00"));

        detalleDomain.setImpuestos(List.of(impuestoDomain));
        facturaDomain.setDetalles(List.of(detalleDomain));

        return facturaDomain;
    }
}
