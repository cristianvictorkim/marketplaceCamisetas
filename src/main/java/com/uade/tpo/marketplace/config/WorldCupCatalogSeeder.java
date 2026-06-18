package com.uade.tpo.marketplace.config;

import com.uade.tpo.marketplace.model.Camiseta;
import com.uade.tpo.marketplace.model.CamisetaTalle;
import com.uade.tpo.marketplace.model.Genero;
import com.uade.tpo.marketplace.model.Pais;
import com.uade.tpo.marketplace.model.Talle;
import com.uade.tpo.marketplace.model.TipoCamiseta;
import com.uade.tpo.marketplace.repository.CamisetaRepository;
import com.uade.tpo.marketplace.repository.CamisetaTalleRepository;
import com.uade.tpo.marketplace.repository.GeneroRepository;
import com.uade.tpo.marketplace.repository.PaisRepository;
import com.uade.tpo.marketplace.repository.TalleRepository;
import com.uade.tpo.marketplace.repository.TipoCamisetaRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Order(100)
@ConditionalOnProperty(name = "app.catalog-seeder.enabled", havingValue = "true", matchIfMissing = true)
public class WorldCupCatalogSeeder implements CommandLineRunner {

    private static final List<String> SIZES = Arrays.asList("S", "M", "L", "XL");

    private final PaisRepository paisRepository;
    private final GeneroRepository generoRepository;
    private final TipoCamisetaRepository tipoCamisetaRepository;
    private final TalleRepository talleRepository;
    private final CamisetaRepository camisetaRepository;
    private final CamisetaTalleRepository camisetaTalleRepository;

    public WorldCupCatalogSeeder(PaisRepository paisRepository,
                                 GeneroRepository generoRepository,
                                 TipoCamisetaRepository tipoCamisetaRepository,
                                 TalleRepository talleRepository,
                                 CamisetaRepository camisetaRepository,
                                 CamisetaTalleRepository camisetaTalleRepository) {
        this.paisRepository = paisRepository;
        this.generoRepository = generoRepository;
        this.tipoCamisetaRepository = tipoCamisetaRepository;
        this.talleRepository = talleRepository;
        this.camisetaRepository = camisetaRepository;
        this.camisetaTalleRepository = camisetaTalleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Genero unisex = ensureGenero("Unisex");
        TipoCamiseta titular = ensureTipo("Titular", true, false);
        TipoCamiseta alternativa = ensureTipo("Alternativa", false, true);

        for (String size : SIZES) {
            ensureTalle(size);
        }

        deactivateInvalidCatalogEntries();

        for (Team team : teams()) {
            Pais pais = ensurePais(team.name, team.group);
            ensureCamiseta(team, pais, unisex, titular, "Titular", new BigDecimal("119.99"), team.homePrimary, team.homeSecondary, team.homeAccent);
            ensureCamiseta(team, pais, unisex, alternativa, "Alternativa", new BigDecimal("109.99"), team.awayPrimary, team.awaySecondary, team.awayAccent);
        }
    }

    private Genero ensureGenero(String name) {
        return generoRepository.findFirstByNombre(name).orElseGet(() -> generoRepository.save(new Genero(name)));
    }

    private TipoCamiseta ensureTipo(String name, boolean titular, boolean alternativa) {
        TipoCamiseta tipo = tipoCamisetaRepository.findFirstByNombre(name)
                .orElseGet(() -> tipoCamisetaRepository.save(new TipoCamiseta(name, titular, alternativa)));
        tipo.setTitular(titular);
        tipo.setAlternativa(alternativa);
        return tipoCamisetaRepository.save(tipo);
    }

    private Talle ensureTalle(String name) {
        return talleRepository.findFirstByNombre(name).orElseGet(() -> talleRepository.save(new Talle(name)));
    }

    private Pais ensurePais(String name, String group) {
        Pais pais = paisRepository.findFirstByNombre(name).orElseGet(() -> new Pais(name, group));
        pais.setGrupoMundial(group);
        return paisRepository.save(pais);
    }

    private void ensureCamiseta(Team team,
                                Pais pais,
                                Genero genero,
                                TipoCamiseta tipo,
                                String version,
                                BigDecimal price,
                                String primary,
                                String secondary,
                                String accent) {
        String name = "Camiseta " + team.name + " " + version + " 2026";
        String image = WorldCupImageCatalog.imageFor(team.name, version);

        if (image == null) {
            camisetaRepository.findFirstByNombre(name).ifPresent(camiseta -> {
                camiseta.setActivo(false);
                camisetaRepository.save(camiseta);
            });
            return;
        }

        String description = "Camiseta " + version.toLowerCase() + " de " + team.name
                + " para el Mundial 2026. Diseno premium inspirado en los colores nacionales, con tela liviana y respirable.";

        Camiseta camiseta = camisetaRepository.findFirstByNombre(name).orElseGet(() ->
                new Camiseta(name, description, price, image, tipo, genero, pais)
        );

        camiseta.setNombre(name);
        camiseta.setDescripcion(description);
        camiseta.setPrecio(price);
        camiseta.setImagen(image);
        camiseta.setTipoCamiseta(tipo);
        camiseta.setGenero(genero);
        camiseta.setPais(pais);
        camiseta.setActivo(true);
        Camiseta saved = camisetaRepository.save(camiseta);

        ensureVariants(saved, team.slug, version, primary);
    }

    private void deactivateInvalidCatalogEntries() {
        for (Camiseta camiseta : camisetaRepository.findAll()) {
            String image = camiseta.getImagen();
            boolean validWebImage = image != null
                    && (image.startsWith("https://") || image.startsWith("http://"));

            if (!validWebImage) {
                camiseta.setActivo(false);
                camisetaRepository.save(camiseta);
                continue;
            }

            if (!camiseta.getNombre().endsWith(" 2026")) {
                continue;
            }

            String expectedImage = WorldCupImageCatalog.imageFor(
                    camiseta.getPais().getNombre(),
                    camiseta.getTipoCamiseta().getNombre()
            );

            if (expectedImage == null || !expectedImage.equals(image)) {
                camiseta.setActivo(false);
                camisetaRepository.save(camiseta);
            }
        }
    }

    private void ensureVariants(Camiseta camiseta, String teamSlug, String version, String color) {
        List<CamisetaTalle> current = camisetaTalleRepository.findByCamisetaId(camiseta.getId());

        for (int index = 0; index < SIZES.size(); index++) {
            String size = SIZES.get(index);
            if (hasSize(current, size)) {
                continue;
            }

            Talle talle = ensureTalle(size);
            int stock = 12 - (index * 2);
            String sku = (teamSlug + "-" + version + "-" + size).toUpperCase().replace(' ', '-');
            CamisetaTalle variante = new CamisetaTalle(camiseta, talle, stock, sku, color);
            camisetaTalleRepository.save(variante);
        }
    }

    private boolean hasSize(List<CamisetaTalle> variants, String size) {
        for (CamisetaTalle variant : variants) {
            if (variant.getTalle().getNombre().equals(size)) {
                return true;
            }
        }
        return false;
    }

    private List<Team> teams() {
        return Arrays.asList(
                new Team("Canada", "canada", "A", "#D71920", "#FFFFFF", "#111111", "#FFFFFF", "#D71920", "#111111"),
                new Team("Mexico", "mexico", "A", "#006341", "#FFFFFF", "#CE1126", "#F3E6C8", "#8A1538", "#006341"),
                new Team("Estados Unidos", "estados-unidos", "D", "#FFFFFF", "#1F4E8C", "#B22234", "#1F4E8C", "#FFFFFF", "#B22234"),
                new Team("Australia", "australia", "D", "#FFCD00", "#0057B8", "#00843D", "#0057B8", "#FFCD00", "#FFFFFF"),
                new Team("Iran", "iran", "G", "#FFFFFF", "#239F40", "#DA0000", "#DA0000", "#FFFFFF", "#239F40"),
                new Team("Japon", "japon", "F", "#003DA5", "#FFFFFF", "#BC002D", "#FFFFFF", "#003DA5", "#BC002D"),
                new Team("Jordania", "jordania", "J", "#FFFFFF", "#CE1126", "#007A3D", "#CE1126", "#000000", "#FFFFFF"),
                new Team("Corea del Sur", "corea-del-sur", "A", "#E6002D", "#FFFFFF", "#0047A0", "#FFFFFF", "#E6002D", "#0047A0"),
                new Team("Qatar", "qatar", "B", "#8A1538", "#FFFFFF", "#A5ACAF", "#FFFFFF", "#8A1538", "#A5ACAF"),
                new Team("Arabia Saudita", "arabia-saudita", "H", "#00843D", "#FFFFFF", "#D6C27A", "#FFFFFF", "#00843D", "#D6C27A"),
                new Team("Uzbekistan", "uzbekistan", "K", "#FFFFFF", "#0099B5", "#1EB53A", "#0099B5", "#FFFFFF", "#CE1126"),
                new Team("Iraq", "iraq", "I", "#CE1126", "#FFFFFF", "#000000", "#FFFFFF", "#CE1126", "#007A3D"),
                new Team("Argelia", "argelia", "J", "#FFFFFF", "#006233", "#D21034", "#006233", "#FFFFFF", "#D21034"),
                new Team("Cabo Verde", "cabo-verde", "H", "#003893", "#FFFFFF", "#CF2027", "#FFFFFF", "#003893", "#F7D116"),
                new Team("RD Congo", "rd-congo", "K", "#007FFF", "#F7D618", "#CE1021", "#FFFFFF", "#007FFF", "#CE1021"),
                new Team("Costa de Marfil", "costa-de-marfil", "E", "#F77F00", "#FFFFFF", "#009E60", "#FFFFFF", "#F77F00", "#009E60"),
                new Team("Egipto", "egipto", "G", "#CE1126", "#FFFFFF", "#000000", "#FFFFFF", "#CE1126", "#C09300"),
                new Team("Ghana", "ghana", "L", "#FFFFFF", "#000000", "#FCD116", "#FCD116", "#006B3F", "#CE1126"),
                new Team("Marruecos", "marruecos", "C", "#C1272D", "#006233", "#F7D618", "#FFFFFF", "#C1272D", "#006233"),
                new Team("Senegal", "senegal", "I", "#FFFFFF", "#00853F", "#FDEF42", "#00853F", "#FDEF42", "#E31B23"),
                new Team("Sudafrica", "sudafrica", "A", "#007A4D", "#FFB612", "#000000", "#FFB612", "#007A4D", "#002395"),
                new Team("Tunez", "tunez", "F", "#FFFFFF", "#E70013", "#111111", "#E70013", "#FFFFFF", "#111111"),
                new Team("Curazao", "curazao", "E", "#002B7F", "#F9E814", "#FFFFFF", "#F9E814", "#002B7F", "#FFFFFF"),
                new Team("Haiti", "haiti", "C", "#00209F", "#D21034", "#FFFFFF", "#D21034", "#00209F", "#FFFFFF"),
                new Team("Panama", "panama", "L", "#FFFFFF", "#D21034", "#005293", "#D21034", "#FFFFFF", "#005293"),
                new Team("Argentina", "argentina", "J", "#75AADB", "#FFFFFF", "#F6B40E", "#1D2951", "#75AADB", "#F6B40E"),
                new Team("Brasil", "brasil", "C", "#FFDF00", "#009739", "#002776", "#002776", "#FFDF00", "#009739"),
                new Team("Colombia", "colombia", "K", "#FCD116", "#003893", "#CE1126", "#003893", "#FCD116", "#CE1126"),
                new Team("Ecuador", "ecuador", "E", "#FFD100", "#003893", "#CE1126", "#003893", "#FFD100", "#CE1126"),
                new Team("Paraguay", "paraguay", "D", "#D52B1E", "#FFFFFF", "#0038A8", "#0038A8", "#FFFFFF", "#D52B1E"),
                new Team("Uruguay", "uruguay", "H", "#6DCFF6", "#FFFFFF", "#FCD116", "#FFFFFF", "#6DCFF6", "#111111"),
                new Team("Nueva Zelanda", "nueva-zelanda", "G", "#111111", "#FFFFFF", "#A2AAAD", "#FFFFFF", "#111111", "#A2AAAD"),
                new Team("Austria", "austria", "J", "#ED2939", "#FFFFFF", "#111111", "#FFFFFF", "#ED2939", "#111111"),
                new Team("Belgica", "belgica", "G", "#ED2939", "#000000", "#FAE042", "#FAE042", "#ED2939", "#000000"),
                new Team("Bosnia y Herzegovina", "bosnia-y-herzegovina", "B", "#002F6C", "#FECB00", "#FFFFFF", "#FFFFFF", "#002F6C", "#FECB00"),
                new Team("Croacia", "croacia", "L", "#FFFFFF", "#E31B23", "#171796", "#171796", "#FFFFFF", "#E31B23"),
                new Team("Chequia", "chequia", "A", "#D7141A", "#FFFFFF", "#11457E", "#11457E", "#FFFFFF", "#D7141A"),
                new Team("Inglaterra", "inglaterra", "L", "#FFFFFF", "#1C2C5B", "#C8102E", "#1C2C5B", "#FFFFFF", "#C8102E"),
                new Team("Francia", "francia", "I", "#002395", "#FFFFFF", "#ED2939", "#FFFFFF", "#002395", "#ED2939"),
                new Team("Alemania", "alemania", "E", "#FFFFFF", "#000000", "#DD0000", "#000000", "#DD0000", "#FFCE00"),
                new Team("Paises Bajos", "paises-bajos", "F", "#FF4F00", "#FFFFFF", "#21468B", "#21468B", "#FFFFFF", "#FF4F00"),
                new Team("Noruega", "noruega", "I", "#BA0C2F", "#FFFFFF", "#00205B", "#00205B", "#FFFFFF", "#BA0C2F"),
                new Team("Portugal", "portugal", "K", "#006600", "#FF0000", "#FFCC00", "#FFFFFF", "#006600", "#FF0000"),
                new Team("Escocia", "escocia", "C", "#003876", "#FFFFFF", "#6A9FD8", "#FFFFFF", "#003876", "#6A9FD8"),
                new Team("Espana", "espana", "H", "#AA151B", "#F1BF00", "#0039F0", "#F1BF00", "#AA151B", "#0039F0"),
                new Team("Suecia", "suecia", "F", "#FECC00", "#006AA7", "#FFFFFF", "#006AA7", "#FECC00", "#FFFFFF"),
                new Team("Suiza", "suiza", "B", "#D52B1E", "#FFFFFF", "#111111", "#FFFFFF", "#D52B1E", "#111111"),
                new Team("Turquia", "turquia", "D", "#E30A17", "#FFFFFF", "#111111", "#FFFFFF", "#E30A17", "#111111")
        );
    }

    private static class Team {
        private final String name;
        private final String slug;
        private final String group;
        private final String homePrimary;
        private final String homeSecondary;
        private final String homeAccent;
        private final String awayPrimary;
        private final String awaySecondary;
        private final String awayAccent;

        private Team(String name,
                     String slug,
                     String group,
                     String homePrimary,
                     String homeSecondary,
                     String homeAccent,
                     String awayPrimary,
                     String awaySecondary,
                     String awayAccent) {
            this.name = name;
            this.slug = slug;
            this.group = group;
            this.homePrimary = homePrimary;
            this.homeSecondary = homeSecondary;
            this.homeAccent = homeAccent;
            this.awayPrimary = awayPrimary;
            this.awaySecondary = awaySecondary;
            this.awayAccent = awayAccent;
        }
    }
}
