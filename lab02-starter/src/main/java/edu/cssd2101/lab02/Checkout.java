package edu.cssd2101.lab02;

import java.util.Objects;

/** Application service depending on catalogue, discount and shipping abstractions. */
public final class Checkout {
    private final DiscountStrategy discount;
    private final MaterialVisitor<Long> shipping;

    /**
     * Wires policies without choosing concrete implementations.
     *
     * @param discount promotion policy
     * @param shipping shipping quotation operation
     * @throws NullPointerException if either policy is null
     */
    public Checkout(DiscountStrategy discount, MaterialVisitor<Long> shipping) {
        this.discount = Objects.requireNonNull(discount);
        this.shipping = Objects.requireNonNull(shipping);
    }

    /**
     * Quotes one copy of each entry from a detached catalogue snapshot.
     *
     * @param catalog nonnull catalogue
     * @param asOfYear pricing context 1450..9999
     * @return total rounded item prices plus shipping, in cents
     * @throws NullPointerException if catalog or a visitor result is null
     * @throws IllegalArgumentException if year, discount or shipping charge is invalid
     * @throws ArithmeticException if total exceeds long range
     */
    public long totalCents(MaterialCatalog catalog, int asOfYear) {
        Objects.requireNonNull(catalog);
        if (asOfYear < 1450 || asOfYear > 9999)
            throw new IllegalArgumentException("invalid pricing year");
        long total = 0;
        for (Material material : catalog.all()) {
            long charge = Objects.requireNonNull(material.accept(shipping));
            if (charge < 0) throw new IllegalArgumentException("shipping must be nonnegative");
            total =
                    Math.addExact(
                            total, Math.addExact(material.priceCents(asOfYear, discount), charge));
        }
        return total;
    }
}
