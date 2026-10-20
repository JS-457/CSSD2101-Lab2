package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

public class PriceCentTraceTest {
    @Test
    void priceCentsDispatchesToSubtypeHookAndStrategy() {
        Material m = MaterialFactory.printed(
                new CatalogData("P1", "Java Design", "Ada", 2000, 2022), 501);

        assertEquals(1500, m.intrinsicBasisPoints(2026));
        assertEquals(1530, m.priceCents(2026, DiscountStrategy.fixed(1000)));
        assertEquals(1700, m.priceCents(2026, DiscountStrategy.none()));
    }
}
