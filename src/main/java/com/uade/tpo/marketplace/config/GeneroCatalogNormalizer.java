package com.uade.tpo.marketplace.config;

import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Order(20)
public class GeneroCatalogNormalizer implements CommandLineRunner {

    private static final String GENERO_CANONICO = "Masculino";
    private static final String GENERO_DUPLICADO = "Hombre";

    private final GeneroRepository generoRepository;
    private final CamisetaRepository camisetaRepository;

    public GeneroCatalogNormalizer(GeneroRepository generoRepository,
                                   CamisetaRepository camisetaRepository) {
        this.generoRepository = generoRepository;
        this.camisetaRepository = camisetaRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Genero masculino = generoRepository.findFirstByNombreIgnoreCase(GENERO_CANONICO)
                .orElseGet(() -> generoRepository.save(new Genero(GENERO_CANONICO)));

        generoRepository.findFirstByNombreIgnoreCase(GENERO_DUPLICADO).ifPresent(hombre -> {
            List<Camiseta> camisetas = camisetaRepository.findByGeneroId(hombre.getId());
            camisetas.forEach(camiseta -> camiseta.setGenero(masculino));
            camisetaRepository.saveAll(camisetas);
            generoRepository.delete(hombre);
        });
    }
}
