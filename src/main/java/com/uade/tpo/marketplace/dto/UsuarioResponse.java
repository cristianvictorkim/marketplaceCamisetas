package com.uade.tpo.marketplace.dto;

public class UsuarioResponse {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String direccion;
    private String telefono;
    private boolean activo;
    private String rol;

    public UsuarioResponse(Long id, String nombre, String apellido, String email,
                           String direccion, String telefono, boolean activo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.activo = activo;
        this.rol = rol;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public String getRol() {
        return rol;
    }
}

