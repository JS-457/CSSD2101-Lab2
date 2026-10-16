package edu.cssd2101.lab02;

import java.util.Objects;

/** Immutable video with detached cast metadata; delivery is explicitly physical or digital. */
public final class VideoMaterial extends Material implements Media {
    private final int durationMinutes;
    private final Delivery delivery;
    private final java.util.List<String> cast;

    /**
     * Creates an edition.
     *
     * @param data validated common metadata
     * @param durationMinutes strictly positive runtime
     * @param delivery nonnull fulfilment mode
     * @param cast nonnull list of nonnull, nonblank names
     * @throws NullPointerException if a required reference is null
     * @throws IllegalArgumentException if duration or a cast name is invalid
     */
    public VideoMaterial(
            CatalogData data, int durationMinutes, Delivery delivery, java.util.List<String> cast) {
        super(data);
        if (durationMinutes <= 0) throw new IllegalArgumentException("duration must be positive");
        this.durationMinutes = durationMinutes;
        this.delivery = Objects.requireNonNull(delivery);
        this.cast = java.util.List.copyOf(Objects.requireNonNull(cast));
        for (String performer : this.cast) CatalogData.text(performer);
    }

    /** {@inheritDoc} */
    @Override
    public int durationMinutes() {
        return durationMinutes;
    }

    /** {@inheritDoc} */
    @Override
    public String format() {
        return delivery == Delivery.PHYSICAL ? "DVD" : "MP4";
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
     * Returns an immutable defensive copy of the supplied cast.
     *
     * @return nonnull cast names
     */
    public java.util.List<String> cast() {
        return cast;
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return data().title() + " (" + format() + ", " + durationMinutes + " min)";
    }

    @Override
    protected int intrinsicBasisPoints(int asOfYear) {
        return asOfYear - data().year() > 5 ? 3000 : (asOfYear - data().year() > 2 ? 1500 : 0);
    }

    /** {@inheritDoc} */
    @Override
    public <R> R accept(MaterialVisitor<R> visitor) {
        return Objects.requireNonNull(visitor).visit(this);
    }
}
