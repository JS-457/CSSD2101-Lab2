package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class BaselineTest {
    @Test
    void polymorphicBaselinePricesAreUseful() {
        Material printed = new PrintedBook(new CatalogData("P", "Java", "Ada", 2000, 2022), 500);
        Material magazine = new Magazine(new CatalogData("M", "Monthly", "Grace", 800, 2025), 12);
        assertEquals(1700, printed.priceCents(2026, DiscountStrategy.none()));
        assertEquals(600, magazine.priceCents(2026, DiscountStrategy.none()));
        MaterialCatalog catalog = new InMemoryCatalog();
        assertTrue(catalog.add(printed));
        assertTrue(catalog.add(magazine));
        assertEquals(List.of(printed), catalog.select(m -> m.id().equals("P")));
        assertFalse(catalog.add(printed));
        assertEquals(2, catalog.all().size());
    }

    @Test
    void baselineSnapshotsAreDetached() {
        MaterialCatalog catalog = new InMemoryCatalog();
        var before = catalog.all();
        catalog.add(new Magazine(new CatalogData("M", "Monthly", "Grace", 0, 2026), 1));
        assertTrue(before.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> catalog.all().clear());
    }
}
