package com.ramirez.sistemafacturacion.config;

import com.ramirez.sistemafacturacion.model.Cliente;
import com.ramirez.sistemafacturacion.model.DetalleFactura;
import com.ramirez.sistemafacturacion.model.Factura;
import com.ramirez.sistemafacturacion.model.ImpuestoDetalleFactura;
import com.ramirez.sistemafacturacion.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(ClienteRepository clienteRepository) {
        return args -> {
            if (clienteRepository.count() > 0) {
                return;
            }

            Cliente clienteUno = new Cliente();
            clienteUno.setNombre("Juan Perez");
            clienteUno.setRfc("PEPJ8001019Q8");
            clienteUno.setEmail("juan.perez@example.com");
            clienteUno.setTelefono("9991002000");
            clienteUno.setCodigoPostal("97000");
            clienteUno.addFactura(crearFacturaConsultoria());

            Cliente clienteDos = new Cliente();
            clienteDos.setNombre("Maria Lopez");
            clienteDos.setRfc("LOMA850515AB1");
            clienteDos.setEmail("maria.lopez@example.com");
            clienteDos.setTelefono("9993004000");
            clienteDos.setCodigoPostal("97100");
            clienteDos.addFactura(crearFacturaSoftware());

            clienteRepository.save(clienteUno);
            clienteRepository.save(clienteDos);
        };
    }

    private Factura crearFacturaConsultoria() {
        Factura factura = new Factura();
        factura.setFolio("FAC-001");
        factura.setCliente("Juan Perez");
        factura.setFecha(LocalDate.of(2026, 7, 27));
        factura.setSubtotal(new BigDecimal("1500.00"));
        factura.setTotalImpuestosTrasladados(new BigDecimal("240.00"));
        factura.setTotalImpuestosRetenidos(BigDecimal.ZERO);
        factura.setTotal(new BigDecimal("1740.00"));

        DetalleFactura consultoria = crearDetalle(
                "Servicio de consultoria",
                2,
                "500.00",
                "1000.00",
                "160.00"
        );
        DetalleFactura soporte = crearDetalle(
                "Soporte tecnico mensual",
                1,
                "500.00",
                "500.00",
                "80.00"
        );

        factura.addDetalle(consultoria);
        factura.addDetalle(soporte);

        return factura;
    }

    private Factura crearFacturaSoftware() {
        Factura factura = new Factura();
        factura.setFolio("FAC-002");
        factura.setCliente("Maria Lopez");
        factura.setFecha(LocalDate.of(2026, 7, 27));
        factura.setSubtotal(new BigDecimal("2500.00"));
        factura.setTotalImpuestosTrasladados(new BigDecimal("400.00"));
        factura.setTotalImpuestosRetenidos(BigDecimal.ZERO);
        factura.setTotal(new BigDecimal("2900.00"));

        DetalleFactura licencia = crearDetalle(
                "Licencia anual de software",
                1,
                "2500.00",
                "2500.00",
                "400.00"
        );

        factura.addDetalle(licencia);

        return factura;
    }

    private DetalleFactura crearDetalle(String descripcion, int cantidad, String precioUnitario, String importe, String iva) {
        DetalleFactura detalle = new DetalleFactura();
        detalle.setDescripcion(descripcion);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(new BigDecimal(precioUnitario));
        detalle.setImporte(new BigDecimal(importe));
        detalle.setObjetoImpuesto("02");
        detalle.addImpuesto(crearIva(importe, iva));
        return detalle;
    }

    private ImpuestoDetalleFactura crearIva(String base, String importe) {
        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(new BigDecimal(base).setScale(6));
        impuesto.setImpuesto("002");
        impuesto.setTipoFactor("Tasa");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(new BigDecimal(importe));
        return impuesto;
    }
}
