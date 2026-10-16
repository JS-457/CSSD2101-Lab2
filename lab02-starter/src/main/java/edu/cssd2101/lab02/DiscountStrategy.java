package edu.cssd2101.lab02;

/** External promotion policy, expressed as basis points (100 basis points = 1 percent). */
@FunctionalInterface
public interface DiscountStrategy {
    /**
     * Computes the extra promotion without changing the material.
     *
     * @param material nonnull material being priced
     * @return additional reduction in 0..10000 basis points
     */
    int extraBasisPoints(Material material);

    /**
     * Creates the no-promotion strategy.
     *
     * @return stateless zero-discount policy
     */
    static DiscountStrategy none() {
        return material -> 0;
    }

    /**
     * Creates a fixed additional reduction.
     *
     * @param basisPoints reduction in 0..10000
     * @return immutable policy
     * @throws IllegalArgumentException if basisPoints is outside the range
     */
    static DiscountStrategy fixed(int basisPoints) {
        if (basisPoints < 0 || basisPoints > 10000)
            throw new IllegalArgumentException("basis points must be 0..10000");
        return material -> basisPoints;
    }
}
