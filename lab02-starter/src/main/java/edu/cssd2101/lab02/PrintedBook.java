package edu.cssd2101.lab02;

import java.util.Objects;

/** Immutable printedbook with validated subtype data. */
public final class PrintedBook extends Material {
    private final int weightGrams;

    /**
     * Creates this material.
     *
     * @param data validated common metadata
     * @param weightGrams physical weight in grams, strictly positive
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if weightGrams is not positive
     */
    public PrintedBook(CatalogData data, int weightGrams) {
        super(data);
        if (weightGrams <= 0) throw new IllegalArgumentException("weightGrams must be positive");
        this.weightGrams = weightGrams;
    }

    /**
     * Returns shipping weight.
     *
     * @return strictly positive grams
     */
    public int weightGrams() {
        return weightGrams;
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return data().title() + " (printed, " + weightGrams + " g)";
    }

    @Override
    protected int intrinsicBasisPoints(int asOfYear) {
        return asOfYear - data().year() > 2 ? 1500 : 0;
    }

    /** {@inheritDoc} */
    @Override
    public <R> R accept(MaterialVisitor<R> visitor) {
        return Objects.requireNonNull(visitor).visit(this);
    }
}
