package com.ramirez.sistemafacturacion.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos para crear o actualizar un cliente")
public class ClienteRequest {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Schema(description = "Nombre del cliente", example = "Carlos Hernandez")
    private String nombre;

    @NotBlank(message = "El RFC del cliente es obligatorio")
    @Schema(description = "RFC del cliente", example = "HEGC900101AB1")
    private String rfc;

    @Email(message = "El correo electronico no tiene un formato valido")
    @Schema(description = "Correo electronico del cliente", example = "carlos.hernandez@example.com")
    private String email;

    @Schema(description = "Telefono del cliente", example = "9995556677")
    private String telefono;

    @Pattern(regexp = "\\d{5}", message = "El codigo postal debe tener 5 digitos")
    @Schema(description = "Codigo postal del cliente", example = "97000")
    private String codigoPostal;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
