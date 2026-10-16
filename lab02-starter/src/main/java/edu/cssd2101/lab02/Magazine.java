package edu.cssd2101.lab02;

import java.util.Objects;

/** Immutable magazine with validated subtype data. */
public final class Magazine extends Material {
    private final int issueNumber;

    /**
     * Creates this material.
     *
     * @param data validated common metadata
     * @param issueNumber positive issue number
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if issueNumber is not positive
     */
    public Magazine(CatalogData data, int issueNumber) {
        super(data);
        if (issueNumber <= 0) throw new IllegalArgumentException("issueNumber must be positive");
        this.issueNumber = issueNumber;
    }

    /**
     * Returns the issue number; it is not a calendar month.
     *
     * @return positive issue number
     */
    public int issueNumber() {
        return issueNumber;
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return data().title() + " (issue " + issueNumber + ")";
    }

    @Override
    protected int intrinsicBasisPoints(int asOfYear) {
        return data().year() < asOfYear ? 2500 : 0;
    }

    /** {@inheritDoc} */
    @Override
    public <R> R accept(MaterialVisitor<R> visitor) {
        return Objects.requireNonNull(visitor).visit(this);
    }
}
