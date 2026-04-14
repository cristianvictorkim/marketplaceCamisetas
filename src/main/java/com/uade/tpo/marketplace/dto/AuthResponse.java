package com.uade.tpo.marketplace.dto;

public class AuthResponse {

    private String token;
    private String tokenType;
    private Long usuarioId;
    private String email;
    private String rol;

    public AuthResponse(String token, Long usuarioId, String email, String rol) {
        this.token = token;
        this.tokenType = "Bearer";
        this.usuarioId = usuarioId;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}
