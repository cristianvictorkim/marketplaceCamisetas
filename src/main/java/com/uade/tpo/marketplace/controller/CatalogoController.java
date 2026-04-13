package com.uade.tpo.marketplace.controller;

import com.uade.tpo.marketplace.dto.GeneroRequest;
import com.uade.tpo.marketplace.dto.PaisRequest;
import com.uade.tpo.marketplace.dto.TalleRequest;
import com.uade.tpo.marketplace.dto.TipoCamisetaRequest;
import com.uade.tpo.marketplace.exception.BusinessException;
import com.uade.tpo.marketplace.exception.ResourceNotFoundException;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.Talle;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import com.uade.tpo.marketplace.repository.PaisRepository;
import com.uade.tpo.marketplace.repository.TalleRepository;
import com.uade.tpo.marketplace.repository.TipoCamisetaRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping("/generos/{id}")
    public Genero getGeneroById(@PathVariable Long id) {
        return findGenero(id);
    }

    @PostMapping("/generos")
    @ResponseStatus(HttpStatus.CREATED)
    public Genero createGenero(@Valid @RequestBody GeneroRequest request) {
        return generoRepository.save(new Genero(request.getNombre()));
    }

    @PutMapping("/generos/{id}")
    public Genero updateGenero(@PathVariable Long id, @Valid @RequestBody GeneroRequest request) {
        Genero genero = findGenero(id);
        genero.setNombre(request.getNombre());
        return generoRepository.save(genero);
    }

    @DeleteMapping("/generos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenero(@PathVariable Long id) {
        Genero genero = findGenero(id);
        deleteCatalogoItem(() -> generoRepository.delete(genero), "Genero is already used and cannot be deleted");
    }

    @GetMapping("/talles")
    public List<Talle> getTalles() {
        return talleRepository.findAll();
    }

    @GetMapping("/talles/{id}")
    public Talle getTalleById(@PathVariable Long id) {
        return findTalle(id);
    }

    @PostMapping("/talles")
    @ResponseStatus(HttpStatus.CREATED)
    public Talle createTalle(@Valid @RequestBody TalleRequest request) {
        return talleRepository.save(new Talle(request.getNombre()));
    }

    @PutMapping("/talles/{id}")
    public Talle updateTalle(@PathVariable Long id, @Valid @RequestBody TalleRequest request) {
        Talle talle = findTalle(id);
        talle.setNombre(request.getNombre());
        return talleRepository.save(talle);
    }

    @DeleteMapping("/talles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTalle(@PathVariable Long id) {
        Talle talle = findTalle(id);
        deleteCatalogoItem(() -> talleRepository.delete(talle), "Talle is already used and cannot be deleted");
    }

    @GetMapping("/tipos-camiseta")
    public List<TipoCamiseta> getTiposCamiseta() {
        return tipoCamisetaRepository.findAll();
    }

    @GetMapping("/tipos-camiseta/{id}")
    public TipoCamiseta getTipoCamisetaById(@PathVariable Long id) {
        return findTipoCamiseta(id);
    }

    @PostMapping("/tipos-camiseta")
    @ResponseStatus(HttpStatus.CREATED)
    public TipoCamiseta createTipoCamiseta(@Valid @RequestBody TipoCamisetaRequest request) {
        validateTipoCamiseta(request);
        return tipoCamisetaRepository.save(new TipoCamiseta(
                request.getNombre(),
                request.isTitular(),
                request.isAlternativa()
        ));
    }

    @PutMapping("/tipos-camiseta/{id}")
    public TipoCamiseta updateTipoCamiseta(@PathVariable Long id, @Valid @RequestBody TipoCamisetaRequest request) {
        validateTipoCamiseta(request);
        TipoCamiseta tipoCamiseta = findTipoCamiseta(id);
        tipoCamiseta.setNombre(request.getNombre());
        tipoCamiseta.setTitular(request.isTitular());
        tipoCamiseta.setAlternativa(request.isAlternativa());
        return tipoCamisetaRepository.save(tipoCamiseta);
    }

    @DeleteMapping("/tipos-camiseta/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTipoCamiseta(@PathVariable Long id) {
        TipoCamiseta tipoCamiseta = findTipoCamiseta(id);
        deleteCatalogoItem(() -> tipoCamisetaRepository.delete(tipoCamiseta), "Tipo camiseta is already used and cannot be deleted");
    }

    @GetMapping("/paises")
    public List<Pais> getPaises() {
        return paisRepository.findAll();
    }

    @GetMapping("/paises/{id}")
    public Pais getPaisById(@PathVariable Long id) {
        return findPais(id);
    }

    @PostMapping("/paises")
    @ResponseStatus(HttpStatus.CREATED)
    public Pais createPais(@Valid @RequestBody PaisRequest request) {
        return paisRepository.save(new Pais(request.getNombre(), request.getGrupoMundial()));
    }

    @PutMapping("/paises/{id}")
    public Pais updatePais(@PathVariable Long id, @Valid @RequestBody PaisRequest request) {
        Pais pais = findPais(id);
        pais.setNombre(request.getNombre());
        pais.setGrupoMundial(request.getGrupoMundial());
        return paisRepository.save(pais);
    }

    @DeleteMapping("/paises/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePais(@PathVariable Long id) {
        Pais pais = findPais(id);
        deleteCatalogoItem(() -> paisRepository.delete(pais), "Pais is already used and cannot be deleted");
    }

    private Genero findGenero(Long id) {
        return generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genero not found with id " + id));
    }

    private Talle findTalle(Long id) {
        return talleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Talle not found with id " + id));
    }

    private TipoCamiseta findTipoCamiseta(Long id) {
        return tipoCamisetaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo camiseta not found with id " + id));
    }

    private Pais findPais(Long id) {
        return paisRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pais not found with id " + id));
    }

    private void validateTipoCamiseta(TipoCamisetaRequest request) {
        if (request.isTitular() && request.isAlternativa()) {
            throw new BusinessException("Tipo camiseta cannot be titular and alternativa at the same time");
        }
    }

    private void deleteCatalogoItem(Runnable deleteAction, String message) {
        try {
            deleteAction.run();
        } catch (DataIntegrityViolationException exception) {
            throw new BusinessException(message);
        }
    }
}
