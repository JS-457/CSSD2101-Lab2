package edu.cssd2101.lab02;

import java.util.Objects;

/** Immutable audio edition; delivery is explicitly physical or digital. */
public final class AudioBook extends Material implements Media {
    private final int durationMinutes;
    private final Delivery delivery;
    private final boolean abridged;

    /**
     * Creates an edition.
     *
     * @param data validated common metadata
     * @param durationMinutes strictly positive runtime
     * @param delivery nonnull fulfilment mode
     * @param abridged whether content is shortened
     * @throws NullPointerException if a required reference is null
     * @throws IllegalArgumentException if duration or a cast name is invalid
     */
    public AudioBook(CatalogData data, int durationMinutes, Delivery delivery, boolean abridged) {
        super(data);
        if (durationMinutes <= 0) throw new IllegalArgumentException("duration must be positive");
        this.durationMinutes = durationMinutes;
        this.delivery = Objects.requireNonNull(delivery);
        this.abridged = abridged;
    }

    /** {@inheritDoc} */
    @Override
    public int durationMinutes() {
        return durationMinutes;
    }

    /** {@inheritDoc} */
    @Override
    public String format() {
        return delivery == Delivery.PHYSICAL ? "CD" : "MP3";
    }

    /**
     * Returns fulfilment mode.
     *
     * @return physical or digital
     */
    public Delivery delivery() {
        return delivery;
    }

    /**
     * Reports whether content is abridged.
     *
     * @return true for abridged editions
     */
    public boolean abridged() {
        return abridged;
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return data().title() + " (" + format() + ", " + durationMinutes + " min)";
    }

    @Override
    protected int intrinsicBasisPoints(int asOfYear) {
        return abridged ? 1000 : 0;
    }

    /** {@inheritDoc} */
    @Override
    public <R> R accept(MaterialVisitor<R> visitor) {
        return Objects.requireNonNull(visitor).visit(this);
    }
}
