package com.uade.tpo.marketplace.config;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WorldCupPriceCatalogTest {

    @Test
    void assignsPriceByFifaRankingTier() {
        assertEquals(new BigDecimal("150.00"), WorldCupPriceCatalog.priceFor("Argentina"));
        assertEquals(new BigDecimal("120.00"), WorldCupPriceCatalog.priceFor("Mexico"));
        assertEquals(new BigDecimal("100.00"), WorldCupPriceCatalog.priceFor("Canada"));
        assertEquals(new BigDecimal("75.00"), WorldCupPriceCatalog.priceFor("Nueva Zelanda"));
    }
}
