package com.ramirez.sistemafacturacion.controller;

import com.ramirez.sistemafacturacion.domain.DetalleFacturaDomain;
import com.ramirez.sistemafacturacion.service.DetalleFacturaService;
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

@Tag(name = "Detalles de factura", description = "Endpoints para consultar conceptos de factura")
@RestController
@RequestMapping("/api/detalles-factura")
public class DetalleFacturaController {

    private final DetalleFacturaService detalleFacturaService;

    public DetalleFacturaController(DetalleFacturaService detalleFacturaService) {
        this.detalleFacturaService = detalleFacturaService;
    }

    @Operation(summary = "Get all invoice details", description = "Get all invoice details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Invoice details found"),
            @ApiResponse(responseCode = "404", description = "No invoice details found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<DetalleFacturaDomain>> getAll() {
        List<DetalleFacturaDomain> detalles = detalleFacturaService.findAll();
        if (detalles.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }
}
