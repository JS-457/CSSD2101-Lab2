package edu.cssd2101.lab02;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Closed family of immutable materials with shared identity and a final price algorithm.
 *
 * <p>Identity is the catalogue ID across all subtypes. A sealed hierarchy makes every shipping case
 * explicit. Adding a subtype requires extending its visitor.
 */
public abstract sealed class Material
        permits PrintedBook, Magazine, AudioBook, VideoMaterial, EBook {
    private final CatalogData data;

    /**
     * Initializes shared immutable metadata.
     *
     * @param data validated metadata
     * @throws NullPointerException if data is null
     */
    protected Material(CatalogData data) {
        this.data = Objects.requireNonNull(data, "data");
    }

    /**
     * Returns immutable common metadata.
     *
     * @return metadata
     */
    public final CatalogData data() {
        return data;
    }

    /**
     * Returns catalogue identity.
     *
     * @return nonblank ID
     */
    public final String id() {
        return data.id();
    }

    /**
     * Describes concrete behavior for a human reader.
     *
     * @return nonblank description
     */
    public abstract String description();

    /**
     * Gives the built-in discount hook.
     *
     * @param asOfYear validated pricing year
     * @return reduction in 0..10000 basis points
     */
    protected abstract int intrinsicBasisPoints(int asOfYear);

    /**
     * Applies intrinsic and external reductions multiplicatively, then rounds once HALF_EVEN.
     *
     * <p>For 101 cents with 15 percent intrinsic and 10 percent extra, the unrounded amount is
     * 77.265 cents, so the result is 77 cents. Input objects are never changed.
     *
     * @param asOfYear pricing context year, 1450..9999
     * @param strategy nonnull extra-discount policy
     * @return nonnegative rounded cents, no greater than the base price
     * @throws NullPointerException if strategy is null
     * @throws IllegalArgumentException if year or any discount is out of range
     */
    public final long priceCents(int asOfYear, DiscountStrategy strategy) {
        Objects.requireNonNull(strategy, "strategy");
        if (asOfYear < 1450 || asOfYear > 9999)
            throw new IllegalArgumentException("invalid pricing year");
        int intrinsic = intrinsicBasisPoints(asOfYear);
        int extra = strategy.extraBasisPoints(this);
        if (intrinsic < 0 || intrinsic > 10000 || extra < 0 || extra > 10000)
            throw new IllegalArgumentException("discount must be 0..10000 basis points");
        return BigDecimal.valueOf(data.priceCents())
                .multiply(BigDecimal.valueOf(10000L - intrinsic))
                .multiply(BigDecimal.valueOf(10000L - extra))
                .divide(BigDecimal.valueOf(100_000_000), 0, RoundingMode.HALF_EVEN)
                .longValueExact();
    }

    /**
     * Double-dispatches to a type-specific visitor method.
     *
     * @param <R> visitor result type
     * @param visitor nonnull operation
     * @return visitor result
     * @throws NullPointerException if visitor is null
     */
    public abstract <R> R accept(MaterialVisitor<R> visitor);

    /**
     * Tests global catalogue identity.
     *
     * @param other candidate
     * @return same ID
     */
    @Override
    public final boolean equals(Object other) {
        return other instanceof Material m && id().equals(m.id());
    }

    /**
     * Hashes global identity.
     *
     * @return stable ID hash
     */
    @Override
    public final int hashCode() {
        return id().hashCode();
    }

    /**
     * Describes identity and concrete display behavior.
     *
     * @return stable description
     */
    @Override
    public final String toString() {
        return id() + ": " + description();
    }
}
