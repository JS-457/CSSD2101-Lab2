package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EBookBuilderTest {
    private static CatalogData data() {
        return new CatalogData("E1", "Title", "Ada", 101, 2026);
    }

    @Test
    void buildUsesEpubAndDrmEnabledByDefault() {
        EBook book = MaterialFactory.ebook(data(), 1024, 251).build();
        assertEquals("EPUB", book.format());
        assertTrue(book.drmEnabled());
        assertEquals(1024, book.fileBytes());
        assertEquals(251, book.wordCount());
        assertEquals("E1", book.id());
    }

    @Test
    void buildUsesChangedOptions() {
        EBook book = MaterialFactory.ebook(data(), 1024, 251)
                .format(EBookFormat.MOBI)
                .drm(false)
                .build();
        assertEquals("MOBI", book.format());
        assertFalse(book.drmEnabled());
    }

    @Test
    void reusedBuilderCreatesIndependentProducts() {
        EBookBuilder builder = MaterialFactory.ebook(data(), 1024, 251);
        EBook first = builder.build();
        EBook second = builder.format(EBookFormat.PDF).drm(false).build();
        assertTrue(first != second);
        assertEquals("EPUB", first.format());
        assertTrue(first.drmEnabled());
        assertEquals("PDF", second.format());
        assertFalse(second.drmEnabled());
    }

    @Test
    void rejectedNullFormatKeepsPreviousSetting() {
        EBookBuilder builder = MaterialFactory.ebook(data(), 1024, 251).format(EBookFormat.PDF);
        assertThrows(NullPointerException.class, () -> builder.format(null));
        assertEquals("PDF", builder.build().format());
    }

    @Test
    void invalidRequiredFieldsAreRejectedWhenBuilderIsCreated() {
        assertThrows(NullPointerException.class, () -> MaterialFactory.ebook(null, 1024, 1));
        assertThrows(IllegalArgumentException.class, () -> MaterialFactory.ebook(data(), 0, 1));
        assertThrows(IllegalArgumentException.class, () -> MaterialFactory.ebook(data(), 1024, -1));
    }
}
