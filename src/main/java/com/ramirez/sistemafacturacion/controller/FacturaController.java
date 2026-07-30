package com.ramirez.sistemafacturacion.controller;

import com.ramirez.sistemafacturacion.domain.DetalleFacturaDomain;
import com.ramirez.sistemafacturacion.domain.FacturaDomain;
import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.service.FacturaService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Invoices", description = "Endpoints to manage invoices with their details and taxes")
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @Operation(summary = "Get all invoices", description = "Get all invoices with details and taxes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoices found"),
            @ApiResponse(responseCode = "404", description = "No invoices found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<FacturaDomain>> getAll() {
        List<FacturaDomain> facturas = facturaService.findAll();
        if (facturas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    @Operation(summary = "Get invoices by client ID", description = "Get all invoices associated with a registered client.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client invoices found"),
            @ApiResponse(responseCode = "400", description = "Wrong client ID"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<FacturaDomain>> getByClienteId(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Integer clienteId) {
        return facturaService.findByClienteId(clienteId)
                .map(facturas -> new ResponseEntity<>(facturas, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create invoice", description = "Add an invoice with details and taxes.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Invoice saved"),
            @ApiResponse(responseCode = "400", description = "Wrong invoice data"),
            @ApiResponse(responseCode = "409", description = "Invoice exists already"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/save")
    public ResponseEntity<FacturaDomain> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Invoice data. The clienteId must exist when it is sent.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FacturaRequest.class),
                            examples = @ExampleObject(
                                    name = "Valid invoice request",
                                    summary = "New invoice example",
                                    value = """
                                            {
                                              "folio": "FAC-003",
                                              "clienteId": 1,
                                              "cliente": "Juan Perez",
                                              "fecha": "2026-07-27",
                                              "subtotal": 1500.00,
                                              "totalImpuestosTrasladados": 240.00,
                                              "totalImpuestosRetenidos": 0.00,
                                              "total": 1740.00,
                                              "detalles": [
                                                    {
                                                      "descripcion": "Consulting service",
                                                  "cantidad": 2,
                                                  "precioUnitario": 500.00,
                                                  "importe": 1000.00,
                                                  "objetoImpuesto": "02",
                                                  "impuestos": [
                                                    {
                                                      "tipo": "TRASLADO",
                                                      "base": 1000.000000,
                                                      "impuesto": "002",
                                                      "tipoFactor": "Tasa",
                                                      "tasaOCuota": 0.160000,
                                                      "importe": 160.00
                                                    }
                                                  ]
                                                },
                                                    {
                                                      "descripcion": "Software license",
                                                  "cantidad": 1,
                                                  "precioUnitario": 500.00,
                                                  "importe": 500.00,
                                                  "objetoImpuesto": "02",
                                                  "impuestos": [
                                                    {
                                                      "tipo": "TRASLADO",
                                                      "base": 500.000000,
                                                      "impuesto": "002",
                                                      "tipoFactor": "Tasa",
                                                      "tasaOCuota": 0.160000,
                                                      "importe": 80.00
                                                    }
                                                  ]
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody FacturaRequest request) {
        return new ResponseEntity<>(facturaService.save(toFacturaDomain(request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete invoice by ID", description = "Delete an invoice with its details and taxes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice deleted"),
            @ApiResponse(responseCode = "400", description = "Wrong invoice ID"),
            @ApiResponse(responseCode = "404", description = "Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Invoice ID", example = "1")
            @PathVariable Integer id) {
        if (facturaService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private FacturaDomain toFacturaDomain(FacturaRequest request) {
        FacturaDomain factura = new FacturaDomain();
        factura.setFolio(request.getFolio());
        factura.setClienteId(request.getClienteId());
        factura.setCliente(request.getCliente());
        factura.setFecha(request.getFecha());
        factura.setSubtotal(request.getSubtotal());
        factura.setTotalImpuestosTrasladados(request.getTotalImpuestosTrasladados());
        factura.setTotalImpuestosRetenidos(request.getTotalImpuestosRetenidos());
        factura.setTotal(request.getTotal());
        factura.setDetalles(toDetalles(request.getDetalles()));
        return factura;
    }

    private List<DetalleFacturaDomain> toDetalles(List<DetalleFacturaRequest> requests) {
        if (requests == null) {
            return null;
        }

        List<DetalleFacturaDomain> detalles = new ArrayList<>(requests.size());
        for (DetalleFacturaRequest request : requests) {
            DetalleFacturaDomain detalle = new DetalleFacturaDomain();
            detalle.setDescripcion(request.getDescripcion());
            detalle.setCantidad(request.getCantidad());
            detalle.setPrecioUnitario(request.getPrecioUnitario());
            detalle.setImporte(request.getImporte());
            detalle.setObjetoImpuesto(request.getObjetoImpuesto());
            detalle.setImpuestos(toImpuestos(request.getImpuestos()));
            detalles.add(detalle);
        }
        return detalles;
    }

    private List<ImpuestoDetalleFacturaDomain> toImpuestos(List<ImpuestoDetalleFacturaRequest> requests) {
        if (requests == null) {
            return null;
        }

        List<ImpuestoDetalleFacturaDomain> impuestos = new ArrayList<>(requests.size());
        for (ImpuestoDetalleFacturaRequest request : requests) {
            ImpuestoDetalleFacturaDomain impuesto = new ImpuestoDetalleFacturaDomain();
            impuesto.setTipo(request.getTipo());
            impuesto.setBase(request.getBase());
            impuesto.setImpuesto(request.getImpuesto());
            impuesto.setTipoFactor(request.getTipoFactor());
            impuesto.setTasaOCuota(request.getTasaOCuota());
            impuesto.setImporte(request.getImporte());
            impuestos.add(impuesto);
        }
        return impuestos;
    }
}
