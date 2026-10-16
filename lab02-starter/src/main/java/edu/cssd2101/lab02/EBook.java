package edu.cssd2101.lab02;

import java.util.Objects;

/** Immutable downloadable e-book, with a 15 percent intrinsic discount when DRM is absent. */
public final class EBook extends Material implements Media {
    private final EBookFormat format;
    private final long fileBytes;
    private final int wordCount;
    private final boolean drmEnabled;

    /**
     * Creates validated digital content.
     *
     * @param data common metadata
     * @param format supported nonnull format
     * @param fileBytes strictly positive exact byte count
     * @param wordCount nonnegative word count
     * @param drmEnabled whether digital rights management is enabled
     * @throws NullPointerException if data or format is null
     * @throws IllegalArgumentException if fileBytes or wordCount is invalid
     */
    public EBook(
            CatalogData data,
            EBookFormat format,
            long fileBytes,
            int wordCount,
            boolean drmEnabled) {
        super(data);
        this.format = Objects.requireNonNull(format, "format");
        if (fileBytes <= 0 || wordCount < 0)
            throw new IllegalArgumentException("positive bytes and nonnegative words required");
        this.fileBytes = fileBytes;
        this.wordCount = wordCount;
        this.drmEnabled = drmEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public String format() {
        return format.name();
    }

    /**
     * Returns exact payload size.
     *
     * @return positive byte count
     */
    public long fileBytes() {
        return fileBytes;
    }

    /**
     * Returns word count.
     *
     * @return nonnegative words
     */
    public int wordCount() {
        return wordCount;
    }

    /**
     * Reports DRM setting.
     *
     * @return true if enabled
     */
    public boolean drmEnabled() {
        return drmEnabled;
    }

    /**
     * Estimates reading time at 250 words per minute, rounding up; zero words takes zero minutes.
     *
     * @return nonnegative minutes, safely calculated for every int word count
     */
    public int readingTimeMinutes() {
        // TODO T2: implement the documented extension contract.
        throw new UnsupportedOperationException("T2 is an exercise");
    }

    /** {@inheritDoc} */
    @Override
    public int durationMinutes() {
        return readingTimeMinutes();
    }

    /** {@inheritDoc} */
    @Override
    public String description() {
        return data().title() + " (" + format() + ", " + wordCount + " words)";
    }

    @Override
    protected int intrinsicBasisPoints(int asOfYear) {
        // TODO T2: implement the documented extension contract.
        throw new UnsupportedOperationException("T2 is an exercise");
    }

    /** {@inheritDoc} */
    @Override
    public <R> R accept(MaterialVisitor<R> visitor) {
        return Objects.requireNonNull(visitor).visit(this);
    }
}
