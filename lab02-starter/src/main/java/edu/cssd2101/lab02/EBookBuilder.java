package edu.cssd2101.lab02;

import java.util.Objects;

/**
 * Reusable e-book builder: mandatory data, bytes and words; optional EPUB format and DRM enabled.
 */
public final class EBookBuilder {
    private final CatalogData data;
    private final long bytes;
    private final int words;
    private EBookFormat format = EBookFormat.EPUB;
    private boolean drm = true;

    /**
     * Initializes required fields; validates them through the same domain constructor.
     *
     * @param data common metadata
     * @param bytes positive payload size
     * @param words nonnegative word count
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if bytes or words is invalid
     */
    public EBookBuilder(CatalogData data, long bytes, int words) {
        new EBook(data, format, bytes, words, drm);
        this.data = data;
        this.bytes = bytes;
        this.words = words;
    }

    /**
     * Selects a supported format, leaving the builder unchanged on null input.
     *
     * @param format nonnull format
     * @return this builder
     * @throws NullPointerException if format is null
     */
    public EBookBuilder format(EBookFormat format) {
        this.format = Objects.requireNonNull(format);
        return this;
    }

    /**
     * Selects the DRM policy.
     *
     * @param enabled true to enable DRM
     * @return this builder
     */
    public EBookBuilder drm(boolean enabled) {
        drm = enabled;
        return this;
    }

    /**
     * Constructs an independent immutable product.
     *
     * @return validated e-book
     */
    public EBook build() {
        // TODO T5: implement the documented extension contract.
        return new EBook(data, format, bytes, words, drm);
    }
}
