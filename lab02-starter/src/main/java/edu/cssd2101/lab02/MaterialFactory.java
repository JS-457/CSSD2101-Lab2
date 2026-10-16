package edu.cssd2101.lab02;

/** Named, typed creation entry points. Constructor validation remains authoritative. */
public final class MaterialFactory {
    private MaterialFactory() {}

    /**
     * Creates a printed edition.
     *
     * @param data common metadata
     * @param grams positive shipping weight
     * @return printed book
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if grams is invalid
     */
    public static PrintedBook printed(CatalogData data, int grams) {
        return new PrintedBook(data, grams);
    }

    /**
     * Creates a magazine issue.
     *
     * @param data common metadata
     * @param issue positive issue number
     * @return magazine
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if issue is invalid
     */
    public static Magazine magazine(CatalogData data, int issue) {
        return new Magazine(data, issue);
    }

    /**
     * Creates an audio edition.
     *
     * @param data common metadata
     * @param minutes positive runtime
     * @param delivery nonnull fulfilment mode
     * @param abridged whether shortened
     * @return audiobook
     * @throws NullPointerException if data or delivery is null
     * @throws IllegalArgumentException if minutes is invalid
     */
    public static AudioBook audio(
            CatalogData data, int minutes, Delivery delivery, boolean abridged) {
        return new AudioBook(data, minutes, delivery, abridged);
    }

    /**
     * Creates a video edition and copies its cast list.
     *
     * @param data common metadata
     * @param minutes positive runtime
     * @param delivery nonnull fulfilment mode
     * @param cast nonnull nonblank cast names
     * @return video
     * @throws NullPointerException if a required reference is null
     * @throws IllegalArgumentException if runtime or a name is invalid
     */
    public static VideoMaterial video(
            CatalogData data, int minutes, Delivery delivery, java.util.List<String> cast) {
        return new VideoMaterial(data, minutes, delivery, cast);
    }

    /**
     * Starts a typed e-book builder.
     *
     * @param data common metadata
     * @param bytes positive payload size
     * @param words nonnegative word count
     * @return new builder with EPUB and DRM defaults
     * @throws NullPointerException if data is null
     * @throws IllegalArgumentException if bytes or words is invalid
     */
    public static EBookBuilder ebook(CatalogData data, long bytes, int words) {
        return new EBookBuilder(data, bytes, words);
    }
}
