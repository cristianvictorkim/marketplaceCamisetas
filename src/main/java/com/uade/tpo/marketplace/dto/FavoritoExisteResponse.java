package com.uade.tpo.marketplace.dto;

public class FavoritoExisteResponse {

    private Long camisetaId;
    private boolean favorito;

    public FavoritoExisteResponse(Long camisetaId, boolean favorito) {
        this.camisetaId = camisetaId;
        this.favorito = favorito;
    }

    public Long getCamisetaId() {
        return camisetaId;
    }

    public boolean isFavorito() {
        return favorito;
    }
}
