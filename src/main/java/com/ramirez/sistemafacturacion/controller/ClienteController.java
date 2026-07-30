package com.ramirez.sistemafacturacion.controller;

import com.ramirez.sistemafacturacion.domain.ClienteDomain;
import com.ramirez.sistemafacturacion.service.ClienteService;
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

import java.util.List;

@Tag(name = "Clients", description = "Endpoints to manage registered clients")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Get all clients", description = "Get all registered clients.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clients found"),
            @ApiResponse(responseCode = "404", description = "No clients found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ClienteDomain>> getAll() {
        List<ClienteDomain> clientes = clienteService.findAll();
        if (clientes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @Operation(summary = "Get client by ID", description = "Get one registered client by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "400", description = "Wrong client ID"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDomain> getById(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Integer id) {
        return clienteService.getById(id)
                .map(cliente -> new ResponseEntity<>(cliente, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Create client", description = "Add a client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client saved"),
            @ApiResponse(responseCode = "400", description = "Wrong client data"),
            @ApiResponse(responseCode = "409", description = "Client exists already"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping("/save")
    public ResponseEntity<ClienteDomain> save(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Client data.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ClienteRequest.class),
                            examples = @ExampleObject(
                                    name = "Valid client request",
                                    summary = "New client example",
                                    value = """
                                            {
                                              "nombre": "Carlos Hernandez",
                                              "rfc": "HEGC900101AB1",
                                              "email": "carlos.hernandez@example.com",
                                              "telefono": "9995556677",
                                              "codigoPostal": "97000"
                                            }
                                            """
                            )
                    )
            )
            @Valid @RequestBody ClienteRequest request) {
        return new ResponseEntity<>(clienteService.save(toClienteDomain(request)), HttpStatus.CREATED);
    }

    @Operation(summary = "Delete client by ID", description = "Delete a client if it exists.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Client deleted"),
            @ApiResponse(responseCode = "400", description = "Wrong client ID"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Client ID", example = "1")
            @PathVariable Integer id) {
        if (clienteService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ClienteDomain toClienteDomain(ClienteRequest request) {
        ClienteDomain cliente = new ClienteDomain();
        cliente.setNombre(request.getNombre());
        cliente.setRfc(request.getRfc());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setCodigoPostal(request.getCodigoPostal());
        return cliente;
    }
}
