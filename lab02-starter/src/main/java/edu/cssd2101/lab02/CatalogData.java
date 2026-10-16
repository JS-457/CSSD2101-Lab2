package edu.cssd2101.lab02;

import java.util.Objects;

/**
 * Immutable shared metadata, with edition-specific years 1450..2027.
 *
 * @param id unique nonblank catalogue identifier (not necessarily an ISBN)
 * @param title nonblank display title
 * @param creator nonblank primary creator
 * @param priceCents nonnegative exact cents
 * @param year publication year in 1450..2027
 */
public record CatalogData(String id, String title, String creator, long priceCents, int year) {
    /**
     * Validates and strips text; performs no I/O or clock access.
     *
     * @param id catalogue identifier
     * @param title display title
     * @param creator primary creator
     * @param priceCents nonnegative exact cents
     * @param year publication year
     * @throws NullPointerException if any string is null
     * @throws IllegalArgumentException if text is blank, cents negative or year out of range
     */
    public CatalogData {
        id = text(id);
        title = text(title);
        creator = text(creator);
        if (priceCents < 0) throw new IllegalArgumentException("priceCents must be nonnegative");
        if (year < 1450 || year > 2027)
            throw new IllegalArgumentException("year must be 1450..2027");
    }

    static String text(String value) {
        String normalized = Objects.requireNonNull(value, "text").strip();
        if (normalized.isBlank()) throw new IllegalArgumentException("text must not be blank");
        return normalized;
    }
}
