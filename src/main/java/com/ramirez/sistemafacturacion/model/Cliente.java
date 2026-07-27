package com.ramirez.sistemafacturacion.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @NotBlank(message = "El RFC del cliente es obligatorio")
    @Column(name = "rfc", nullable = false, unique = true, length = 13)
    private String rfc;

    @Email(message = "El correo electronico no tiene un formato valido")
    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Pattern(regexp = "\\d{5}", message = "El codigo postal debe tener 5 digitos")
    @Column(name = "codigo_postal", length = 5)
    private String codigoPostal;

    @JsonIgnore
    @OneToMany(mappedBy = "clienteRegistro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Factura> facturas = new ArrayList<>();

    public Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas.clear();
        if (facturas != null) {
            facturas.forEach(this::addFactura);
        }
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
        factura.setClienteRegistro(this);
    }

    public void removeFactura(Factura factura) {
        facturas.remove(factura);
        factura.setClienteRegistro(null);
    }
}
