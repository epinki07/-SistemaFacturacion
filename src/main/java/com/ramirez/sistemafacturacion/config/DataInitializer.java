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
import java.math.RoundingMode;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(ClienteRepository clienteRepository) {
        return args -> {
            if (clienteRepository.count() > 0) {
                return;
            }

            Cliente juan = crearCliente("Juan Perez", "PEPJ8001019Q8", "juan.perez@example.com", "9991002000", "97000");
            juan.addFactura(crearFactura("FAC-001", juan.getNombre(), LocalDate.of(2026, 7, 27),
                    crearDetalle("Consulting service", 2, "500.00"),
                    crearDetalle("Monthly technical support", 1, "500.00")));
            juan.addFactura(crearFactura("FAC-002", juan.getNombre(), LocalDate.of(2026, 7, 28),
                    crearDetalle("Business hosting", 1, "800.00"),
                    crearDetalle("Annual domain renewal", 1, "350.00")));

            Cliente maria = crearCliente("Maria Lopez", "LOMA850515AB1", "maria.lopez@example.com", "9993004000", "97100");
            maria.addFactura(crearFactura("FAC-003", maria.getNombre(), LocalDate.of(2026, 7, 28),
                    crearDetalle("Annual software license", 1, "2500.00")));
            maria.addFactura(crearFactura("FAC-004", maria.getNombre(), LocalDate.of(2026, 7, 29),
                    crearDetalle("Remote training", 3, "450.00")));

            Cliente michelle = crearCliente("Michelle Rivera", "MIRV960101AB1", "michelle.rivera@example.com", "9994102001", "97010");
            michelle.addFactura(crearFactura("FAC-005", michelle.getNombre(), LocalDate.of(2026, 7, 29),
                    crearDetalle("Website design", 1, "3200.00"),
                    crearDetalle("Initial SEO setup", 1, "900.00")));
            michelle.addFactura(crearFactura("FAC-006", michelle.getNombre(), LocalDate.of(2026, 7, 30),
                    crearDetalle("Monthly website maintenance", 1, "1200.00")));

            Cliente joshua = crearCliente("Joshua Torres", "JOTO970202CD2", "joshua.torres@example.com", "9994102002", "97020");
            joshua.addFactura(crearFactura("FAC-007", joshua.getNombre(), LocalDate.of(2026, 7, 30),
                    crearDetalle("Network equipment", 2, "1450.00"),
                    crearDetalle("Network installation", 1, "1800.00")));
            joshua.addFactura(crearFactura("FAC-008", joshua.getNombre(), LocalDate.of(2026, 7, 31),
                    crearDetalle("Monthly monitoring", 1, "750.00")));

            Cliente josue = crearCliente("Josue Martinez", "JOMA980303EF3", "josue.martinez@example.com", "9994102003", "97030");
            josue.addFactura(crearFactura("FAC-009", josue.getNombre(), LocalDate.of(2026, 7, 31),
                    crearDetalle("Sales module development", 1, "4800.00")));
            josue.addFactura(crearFactura("FAC-010", josue.getNombre(), LocalDate.of(2026, 8, 1),
                    crearDetalle("Integration support", 4, "350.00")));

            Cliente carlos = crearCliente("Carlos Hernandez", "HEGC900101AB1", "carlos.hernandez@example.com", "9994102004", "97040");
            carlos.addFactura(crearFactura("FAC-011", carlos.getNombre(), LocalDate.of(2026, 8, 1),
                    crearDetalle("Basic tax consulting", 1, "1600.00"),
                    crearDetalle("Administrative report", 1, "700.00")));
            carlos.addFactura(crearFactura("FAC-012", carlos.getNombre(), LocalDate.of(2026, 8, 2),
                    crearDetalle("Support plan", 1, "950.00")));

            Cliente jj = crearCliente("JJ Ramirez", "RAJJ990404GH4", "jj.ramirez@example.com", "9994102005", "97050");
            jj.addFactura(crearFactura("FAC-013", jj.getNombre(), LocalDate.of(2026, 8, 2),
                    crearDetalle("License renewal", 5, "420.00")));
            jj.addFactura(crearFactura("FAC-014", jj.getNombre(), LocalDate.of(2026, 8, 3),
                    crearDetalle("Cloud backup service", 1, "1100.00")));

            Cliente angel = crearCliente("Angel Castillo", "CAAA950505IJ5", "angel.castillo@example.com", "9994102006", "97060");
            angel.addFactura(crearFactura("FAC-015", angel.getNombre(), LocalDate.of(2026, 8, 3),
                    crearDetalle("Billing implementation", 1, "5200.00"),
                    crearDetalle("Training session", 2, "600.00")));
            angel.addFactura(crearFactura("FAC-016", angel.getNombre(), LocalDate.of(2026, 8, 4),
                    crearDetalle("Data audit", 1, "2100.00")));

            clienteRepository.save(juan);
            clienteRepository.save(maria);
            clienteRepository.save(michelle);
            clienteRepository.save(joshua);
            clienteRepository.save(josue);
            clienteRepository.save(carlos);
            clienteRepository.save(jj);
            clienteRepository.save(angel);
        };
    }

    private Cliente crearCliente(String nombre, String rfc, String email, String telefono, String codigoPostal) {
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setRfc(rfc);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);
        cliente.setCodigoPostal(codigoPostal);
        return cliente;
    }

    private Factura crearFactura(String folio, String cliente, LocalDate fecha, DetalleFactura... detalles) {
        Factura factura = new Factura();
        factura.setFolio(folio);
        factura.setCliente(cliente);
        factura.setFecha(fecha);

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;
        for (DetalleFactura detalle : detalles) {
            subtotal = subtotal.add(detalle.getImporte());
            totalIva = totalIva.add(detalle.getImpuestos().get(0).getImporte());
            factura.addDetalle(detalle);
        }

        factura.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        factura.setTotalImpuestosTrasladados(totalIva.setScale(2, RoundingMode.HALF_UP));
        factura.setTotalImpuestosRetenidos(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        factura.setTotal(subtotal.add(totalIva).setScale(2, RoundingMode.HALF_UP));
        return factura;
    }

    private DetalleFactura crearDetalle(String descripcion, int cantidad, String precioUnitario) {
        BigDecimal precio = new BigDecimal(precioUnitario);
        BigDecimal importe = precio.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = importe.multiply(new BigDecimal("0.16")).setScale(2, RoundingMode.HALF_UP);

        DetalleFactura detalle = new DetalleFactura();
        detalle.setDescripcion(descripcion);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio.setScale(2, RoundingMode.HALF_UP));
        detalle.setImporte(importe);
        detalle.setObjetoImpuesto("02");
        detalle.addImpuesto(crearIva(importe, iva));
        return detalle;
    }

    private ImpuestoDetalleFactura crearIva(BigDecimal base, BigDecimal importe) {
        ImpuestoDetalleFactura impuesto = new ImpuestoDetalleFactura();
        impuesto.setTipo("TRASLADO");
        impuesto.setBase(base.setScale(6, RoundingMode.HALF_UP));
        impuesto.setImpuesto("002");
        impuesto.setTipoFactor("Tasa");
        impuesto.setTasaOCuota(new BigDecimal("0.160000"));
        impuesto.setImporte(importe.setScale(2, RoundingMode.HALF_UP));
        return impuesto;
    }
}
