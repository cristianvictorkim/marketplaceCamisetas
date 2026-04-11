package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.GeneroRequest;
import com.uade.tpo.marketplace.dto.PaisRequest;
import com.uade.tpo.marketplace.dto.TalleRequest;
import com.uade.tpo.marketplace.dto.TipoCamisetaRequest;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.Talle;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import com.uade.tpo.marketplace.repository.PaisRepository;
import com.uade.tpo.marketplace.repository.TalleRepository;
import com.uade.tpo.marketplace.repository.TipoCamisetaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final GeneroRepository generoRepository;
    private final TalleRepository talleRepository;
    private final TipoCamisetaRepository tipoCamisetaRepository;
    private final PaisRepository paisRepository;

    public CatalogoController(GeneroRepository generoRepository,
                              TalleRepository talleRepository,
                              TipoCamisetaRepository tipoCamisetaRepository,
                              PaisRepository paisRepository) {
        this.generoRepository = generoRepository;
        this.talleRepository = talleRepository;
        this.tipoCamisetaRepository = tipoCamisetaRepository;
        this.paisRepository = paisRepository;
    }

    @GetMapping("/generos")
    public List<Genero> getGeneros() {
        return generoRepository.findAll();
    }

    @PostMapping("/generos")
    @ResponseStatus(HttpStatus.CREATED)
    public Genero createGenero(@Valid @RequestBody GeneroRequest request) {
        return generoRepository.save(new Genero(request.getNombre()));
    }

    @GetMapping("/talles")
    public List<Talle> getTalles() {
        return talleRepository.findAll();
    }

    @PostMapping("/talles")
    @ResponseStatus(HttpStatus.CREATED)
    public Talle createTalle(@Valid @RequestBody TalleRequest request) {
        return talleRepository.save(new Talle(request.getNombre()));
    }

    @GetMapping("/tipos-camiseta")
    public List<TipoCamiseta> getTiposCamiseta() {
        return tipoCamisetaRepository.findAll();
    }

    @PostMapping("/tipos-camiseta")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoCamiseta createTipoCamiseta(@Valid @RequestBody TipoCamisetaRequest request) {
        return tipoCamisetaRepository.save(new TipoCamiseta(
                request.getNombre(),
                request.isTitular(),
                request.isAlternativa()
        ));
    }

    @GetMapping("/paises")
    public List<Pais> getPaises() {
        return paisRepository.findAll();
    }

    @PostMapping("/paises")
    @ResponseStatus(HttpStatus.CREATED)
    public Pais createPais(@Valid @RequestBody PaisRequest request) {
        return paisRepository.save(new Pais(request.getNombre(), request.getGrupoMundial()));
    }
}
