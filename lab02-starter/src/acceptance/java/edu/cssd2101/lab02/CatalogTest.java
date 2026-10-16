package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

class CatalogTest {
    private Material material(String id, String creator, int year) {
        return new PrintedBook(new CatalogData(id, "Title", creator, 100, year), 1);
    }

    @Test
    void mapIdentityMutationsAndSnapshotsPreserveInsertionOrder() {
        MaterialCatalog c = new InMemoryCatalog();
        Material a = material("A", "Ada", 2026), b = material("B", "Grace", 2025);
        assertTrue(c.find("A").isEmpty());
        assertTrue(c.remove("A").isEmpty());
        assertTrue(c.add(a));
        List<Material> snapshot = c.all();
        assertTrue(c.add(b));
        assertFalse(c.add(material("A", "Changed", 2027)));
        assertSame(a, c.find(" A ").orElseThrow());
        assertEquals(List.of(a), snapshot);
        assertThrows(UnsupportedOperationException.class, () -> snapshot.clear());
        assertSame(a, c.remove("A").orElseThrow());
        assertTrue(c.add(a));
        assertEquals(List.of(b, a), c.all());
    }

    @Test
    void queriesUseExplicitCalendarIntervalsAndOrSemantics() {
        MaterialCatalog c = new InMemoryCatalog();
        Material old = material("O", "Ada", 2024),
                last = material("L", "Grace", 2025),
                now = material("N", "Ada", 2026),
                future = material("F", "Lin", 2027);
        for (Material m : List.of(old, last, now, future)) c.add(m);
        assertEquals(List.of(now), c.recent(2026, 1));
        assertEquals(List.of(last, now), c.recent(2026, 2));
        assertEquals(List.of(old, last, now), c.byCreators(" ADA ", "grace", "Ada"));
        assertTrue(c.byCreators().isEmpty());
        assertTrue(c.byCreators("missing").isEmpty());
        assertEquals(List.of(old, now), c.select(m -> m.data().creator().equals("Ada")));
        assertEquals(List.of(future, last, now, old), c.sorted(Comparator.comparing(Material::id)));
        assertEquals(List.of(old, last, now, future), c.all());
        assertEquals(c.all(), c.sorted((a, b) -> 0));
    }

    @Test
    void invalidQueriesAndThrowingCallbacksLeaveStateUnchanged() {
        MaterialCatalog c = new InMemoryCatalog();
        Material a = material("A", "Ada", 2026);
        c.add(a);
        assertThrows(NullPointerException.class, () -> c.add(null));
        assertThrows(NullPointerException.class, () -> c.find(null));
        assertThrows(IllegalArgumentException.class, () -> c.remove(" "));
        assertThrows(NullPointerException.class, () -> c.select(null));
        assertThrows(NullPointerException.class, () -> c.sorted(null));
        assertThrows(NullPointerException.class, () -> c.byCreators((String[]) null));
        assertThrows(NullPointerException.class, () -> c.byCreators("Ada", null));
        assertThrows(IllegalArgumentException.class, () -> c.byCreators(" "));
        for (int n : new int[] {0, -1, 10001})
            assertThrows(IllegalArgumentException.class, () -> c.recent(2026, n));
        assertThrows(IllegalArgumentException.class, () -> c.recent(1449, 1));
        assertThrows(IllegalArgumentException.class, () -> c.recent(10000, 1));
        assertThrows(
                IllegalStateException.class,
                () ->
                        c.select(
                                m -> {
                                    throw new IllegalStateException("predicate failed");
                                }));
        assertEquals(List.of(a), c.all());
    }

    @Test
    void creatorMatchingDoesNotDependOnDefaultLocale() {
        Locale old = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            MaterialCatalog c = new InMemoryCatalog();
            Material m = material("I", "INDIGO", 2026);
            c.add(m);
            assertEquals(List.of(m), c.byCreators("indigo"));
        } finally {
            Locale.setDefault(old);
        }
    }
}
