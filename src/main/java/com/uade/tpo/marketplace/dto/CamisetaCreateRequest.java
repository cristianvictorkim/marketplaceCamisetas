package com.uade.tpo.marketplace.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class CamisetaCreateRequest extends CamisetaRequest {

    @Valid
    @NotEmpty
    private List<CamisetaTalleRequest> variantes = new ArrayList<CamisetaTalleRequest>();

    public CamisetaCreateRequest() {
    }

    public List<CamisetaTalleRequest> getVariantes() {
        return variantes;
    }

    public void setVariantes(List<CamisetaTalleRequest> variantes) {
        this.variantes = variantes;
    }
}
