package com.ramirez.sistemafacturacion.controller;

import com.ramirez.sistemafacturacion.domain.ImpuestoDetalleFacturaDomain;
import com.ramirez.sistemafacturacion.service.ImpuestoDetalleFacturaService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Detail taxes", description = "Endpoints to query and update invoice detail taxes")
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

    @Operation(summary = "Update invoice detail tax", description = "Update only the tax fields of an invoice detail tax.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tax updated"),
            @ApiResponse(responseCode = "400", description = "Wrong tax data"),
            @ApiResponse(responseCode = "404", description = "Tax not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<ImpuestoDetalleFacturaDomain> update(
            @Parameter(description = "Tax ID", example = "1")
            @PathVariable Integer id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Tax data. This endpoint does not change the related invoice detail.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImpuestoDetalleFacturaRequest.class),
                            examples = @ExampleObject(
                                    name = "Valid tax update request",
                                    summary = "Update IVA amount",
                                    value = """
                                            {
                                              "tipo": "TRASLADO",
                                              "base": 1000.000000,
                                              "impuesto": "002",
                                              "tipoFactor": "Tasa",
                                              "tasaOCuota": 0.160000,
                                              "importe": 160.00
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody ImpuestoDetalleFacturaRequest request) {
        return impuestoDetalleFacturaService.update(id, toImpuestoDomain(request))
                .map(impuesto -> new ResponseEntity<>(impuesto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    private ImpuestoDetalleFacturaDomain toImpuestoDomain(ImpuestoDetalleFacturaRequest request) {
        ImpuestoDetalleFacturaDomain impuesto = new ImpuestoDetalleFacturaDomain();
        impuesto.setTipo(request.getTipo());
        impuesto.setBase(request.getBase());
        impuesto.setImpuesto(request.getImpuesto());
        impuesto.setTipoFactor(request.getTipoFactor());
        impuesto.setTasaOCuota(request.getTasaOCuota());
        impuesto.setImporte(request.getImporte());
        return impuesto;
    }
}
