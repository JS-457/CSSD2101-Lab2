package edu.cssd2101.lab02;

/**
 * Type-directed operation. Each concrete material selects its matching overload.
 *
 * @param <R> result type
 */
public interface MaterialVisitor<R> {
    /**
     * Handles a PrintedBook.
     *
     * @param material nonnull concrete material
     * @return operation result
     */
    R visit(PrintedBook material);

    /**
     * Handles a Magazine.
     *
     * @param material nonnull concrete material
     * @return operation result
     */
    R visit(Magazine material);

    /**
     * Handles a AudioBook.
     *
     * @param material nonnull concrete material
     * @return operation result
     */
    R visit(AudioBook material);

    /**
     * Handles a VideoMaterial.
     *
     * @param material nonnull concrete material
     * @return operation result
     */
    R visit(VideoMaterial material);

    /**
     * Handles a EBook.
     *
     * @param material nonnull concrete material
     * @return operation result
     */
    R visit(EBook material);
}
