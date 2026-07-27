package com.ramirez.sistemafacturacion.controller;

import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.service.ImpuestoDetalleFacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Impuestos de detalle", description = "Endpoints para consultar impuestos por concepto")
@RestController
@RequestMapping("/api/impuestos-detalle")
public class ImpuestoDetalleFacturaController {

    private final ImpuestoDetalleFacturaService impuestoDetalleFacturaService;

    public ImpuestoDetalleFacturaController(ImpuestoDetalleFacturaService impuestoDetalleFacturaService) {
        this.impuestoDetalleFacturaService = impuestoDetalleFacturaService;
    }

    @Operation(summary = "Get all invoice detail taxes", description = "Get all taxes applied to invoice details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Taxes found"),
            @ApiResponse(responseCode = "404", description = "No taxes found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ImpuestoDetalleFacturaDomain>> getAll() {
        List<ImpuestoDetalleFacturaDomain> impuestos = impuestoDetalleFacturaService.findAll();
        if (impuestos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(impuestos, HttpStatus.OK);
    }
}
