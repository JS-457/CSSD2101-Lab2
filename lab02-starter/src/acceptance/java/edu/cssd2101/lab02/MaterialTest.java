package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

class MaterialTest {
    private CatalogData data(String id, long cents, int year) {
        return new CatalogData(id, "Title", "Ada", cents, year);
    }

    @Test
    void metadataNormalizesAndRejectsInvalidBoundaries() {
        CatalogData d = new CatalogData(" I ", " T ", " A ", 0, 1450);
        assertEquals("I", d.id());
        assertEquals("T", d.title());
        assertEquals("A", d.creator());
        assertEquals(1450, d.year());
        assertEquals(0, d.priceCents());
        assertThrows(NullPointerException.class, () -> data(null, 0, 2026));
        assertThrows(IllegalArgumentException.class, () -> data(" ", 0, 2026));
        assertThrows(IllegalArgumentException.class, () -> data("I", -1, 2026));
        assertThrows(IllegalArgumentException.class, () -> data("I", 0, 1449));
        assertThrows(IllegalArgumentException.class, () -> data("I", 0, 2028));
        assertEquals(2027, data("I", 0, 2027).year());
    }

    @Test
    void intrinsicDiscountsHaveExplicitYearBoundaries() {
        Material printed = new PrintedBook(data("P", 10000, 2023), 100);
        assertEquals(10000, printed.priceCents(2025, DiscountStrategy.none()));
        assertEquals(8500, printed.priceCents(2026, DiscountStrategy.none()));
        Material magazine = new Magazine(data("M", 10000, 2026), 100);
        assertEquals(10000, magazine.priceCents(2026, DiscountStrategy.none()));
        assertEquals(7500, magazine.priceCents(2027, DiscountStrategy.none()));
        Material video = new VideoMaterial(data("V", 10000, 2020), 60, Delivery.DIGITAL, List.of());
        assertEquals(10000, video.priceCents(2022, DiscountStrategy.none()));
        assertEquals(8500, video.priceCents(2023, DiscountStrategy.none()));
        assertEquals(7000, video.priceCents(2026, DiscountStrategy.none()));
        assertEquals(
                9000,
                new AudioBook(data("A", 10000, 2026), 60, Delivery.PHYSICAL, true)
                        .priceCents(2026, DiscountStrategy.none()));
        assertEquals(
                10000,
                new AudioBook(data("A", 10000, 2026), 60, Delivery.DIGITAL, false)
                        .priceCents(2026, DiscountStrategy.none()));
    }

    @Test
    void templateCombinesStrategiesAndRoundsOnceHalfEven() {
        EBook e = new EBook(data("E", 101, 2026), EBookFormat.EPUB, 1, 251, false);
        assertEquals(86, e.priceCents(2026, DiscountStrategy.none()));
        assertEquals(77, e.priceCents(2026, DiscountStrategy.fixed(1000)));
        assertEquals(0, e.priceCents(2026, DiscountStrategy.fixed(10000)));
        Material odd = new PrintedBook(data("P", 101, 2026), 1);
        assertEquals(50, odd.priceCents(2026, DiscountStrategy.fixed(5000)));
        Material oddUp = new PrintedBook(data("P", 103, 2026), 1);
        assertEquals(52, oddUp.priceCents(2026, DiscountStrategy.fixed(5000)));
        assertEquals(
                Long.MAX_VALUE,
                new PrintedBook(data("X", Long.MAX_VALUE, 2026), 1)
                        .priceCents(2026, DiscountStrategy.none()));
        assertEquals(101, e.data().priceCents());
    }

    @Test
    void invalidStrategyFailsWithoutChangingProduct() {
        Material m = new PrintedBook(data("P", 101, 2026), 1);
        assertThrows(IllegalArgumentException.class, () -> DiscountStrategy.fixed(-1));
        assertThrows(IllegalArgumentException.class, () -> DiscountStrategy.fixed(10001));
        assertThrows(IllegalArgumentException.class, () -> m.priceCents(2026, x -> -1));
        assertThrows(IllegalArgumentException.class, () -> m.priceCents(2026, x -> 10001));
        assertThrows(NullPointerException.class, () -> m.priceCents(2026, null));
        assertThrows(
                IllegalArgumentException.class, () -> m.priceCents(1449, DiscountStrategy.none()));
        assertThrows(
                IllegalArgumentException.class, () -> m.priceCents(10000, DiscountStrategy.none()));
        assertEquals(101, m.priceCents(2026, DiscountStrategy.none()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 249, 250, 251, 2147483647})
    void readingTimeUsesCeilingWithoutOverflow(int words) {
        EBook e = new EBook(data("E", 0, 2026), EBookFormat.PDF, Long.MAX_VALUE, words, true);
        assertEquals((words + 249L) / 250L, e.readingTimeMinutes());
        assertEquals(e.readingTimeMinutes(), e.durationMinutes());
        assertEquals(words, e.wordCount());
        assertEquals(Long.MAX_VALUE, e.fileBytes());
        assertTrue(e.drmEnabled());
        assertEquals("PDF", e.format());
        assertEquals(0, e.priceCents(2026, DiscountStrategy.none()));
    }

    @Test
    void invalidSubtypeDataCannotConstructObjects() {
        CatalogData d = data("X", 0, 2026);
        assertThrows(NullPointerException.class, () -> new PrintedBook(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new PrintedBook(d, 0));
        assertThrows(IllegalArgumentException.class, () -> new Magazine(d, 0));
        assertThrows(
                IllegalArgumentException.class, () -> new AudioBook(d, 0, Delivery.DIGITAL, false));
        assertThrows(NullPointerException.class, () -> new AudioBook(d, 1, null, false));
        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoMaterial(d, 0, Delivery.DIGITAL, List.of()));
        assertThrows(
                IllegalArgumentException.class,
                () -> new VideoMaterial(d, 1, Delivery.DIGITAL, List.of(" ")));
        assertThrows(NullPointerException.class, () -> new EBook(d, null, 1, 0, false));
        assertThrows(
                IllegalArgumentException.class, () -> new EBook(d, EBookFormat.EPUB, 0, 0, false));
        assertThrows(
                IllegalArgumentException.class, () -> new EBook(d, EBookFormat.EPUB, 1, -1, false));
    }

    @Test
    void identityRemainsSymmetricAcrossSubtypes() {
        Material p = new PrintedBook(data("same", 100, 2026), 1);
        Material e = new EBook(data("same", 200, 2026), EBookFormat.MOBI, 1, 0, true);
        assertEquals(p, e);
        assertEquals(e, p);
        assertEquals(p.hashCode(), e.hashCode());
        assertNotEquals(p, null);
        assertNotEquals(p, "same");
        assertNotEquals(p, new Magazine(data("other", 0, 2026), 1));
        assertEquals("same: Title (printed, 1 g)", p.toString());
    }

    @Test
    void videoCastIsDetachedFromCallerAndCannotBeMutated() {
        var input = new ArrayList<>(List.of("Actor"));
        var video = new VideoMaterial(data("V", 0, 2026), 60, Delivery.PHYSICAL, input);
        input.clear();
        assertEquals(List.of("Actor"), video.cast());
        assertThrows(UnsupportedOperationException.class, () -> video.cast().clear());
        assertEquals("DVD", video.format());
        assertEquals(60, video.durationMinutes());
        assertEquals("Title (DVD, 60 min)", video.description());
    }

    @Test
    void typedFactoriesBuilderDefaultsAndReuseAreSafe() {
        CatalogData d = data("E", 100, 2026);
        EBookBuilder builder = MaterialFactory.ebook(d, 1024, 251);
        EBook first = builder.build();
        EBook second = builder.format(EBookFormat.PDF).drm(false).build();
        assertEquals("EPUB", first.format());
        assertTrue(first.drmEnabled());
        assertFalse(second.drmEnabled());
        assertEquals("PDF", second.format());
        assertEquals("Title (PDF, 251 words)", second.description());
        assertThrows(NullPointerException.class, () -> builder.format(null));
        assertEquals("PDF", builder.build().format());
        assertThrows(IllegalArgumentException.class, () -> MaterialFactory.ebook(d, 0, 1));
        assertEquals(1, MaterialFactory.printed(d, 1).weightGrams());
        assertEquals(2, MaterialFactory.magazine(d, 2).issueNumber());
        var a = MaterialFactory.audio(d, 5, Delivery.DIGITAL, true);
        assertTrue(a.abridged());
        assertEquals(5, a.durationMinutes());
        assertEquals("MP3", a.format());
        assertEquals("Title (MP3, 5 min)", a.description());
        assertEquals("MP4", MaterialFactory.video(d, 5, Delivery.DIGITAL, List.of()).format());
    }
}
