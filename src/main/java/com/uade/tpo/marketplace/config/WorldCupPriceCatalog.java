package com.uade.tpo.marketplace.config;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

final class WorldCupPriceCatalog {

    // Ranking masculino oficial FIFA publicado el 11 de junio de 2026.
    // Los bloques contienen únicamente los 48 países presentes en el catálogo.
    private static final Set<String> TOP_12 = new HashSet<String>(Arrays.asList(
            "Argentina", "Espana", "Francia", "Inglaterra",
            "Portugal", "Brasil", "Marruecos", "Paises Bajos",
            "Belgica", "Alemania", "Croacia", "Colombia"
    ));

    private static final Set<String> NEXT_12 = new HashSet<String>(Arrays.asList(
            "Mexico", "Senegal", "Uruguay", "Estados Unidos",
            "Japon", "Suiza", "Iran", "Turquia",
            "Ecuador", "Austria", "Corea del Sur", "Australia"
    ));

    private static final Set<String> THIRD_12 = new HashSet<String>(Arrays.asList(
            "Argelia", "Egipto", "Canada", "Noruega",
            "Costa de Marfil", "Panama", "Suecia", "Chequia",
            "Paraguay", "Escocia", "Tunez", "RD Congo"
    ));

    private WorldCupPriceCatalog() {
    }

    static BigDecimal priceFor(String country) {
        if (TOP_12.contains(country)) {
            return new BigDecimal("150.00");
        }
        if (NEXT_12.contains(country)) {
            return new BigDecimal("120.00");
        }
        if (THIRD_12.contains(country)) {
            return new BigDecimal("100.00");
        }
        return new BigDecimal("75.00");
    }
}
