package edu.cssd2101.lab02;
 
import java.util.Objects;
 
/**
 * Stateless shipping quotation: 50 cents per started 100 g, magazine flat 200, physical CD 50,
 * physical DVD 100, and every digital delivery zero.
 *
 * <p>These are teaching tariff rules in cents, not a live carrier quotation.
 */
public final class ShippingVisitor implements MaterialVisitor<Long> {
    /** Creates a reusable stateless operation. */
    public ShippingVisitor() {}
 
    /** {@inheritDoc} */
    @Override
    public Long visit(PrintedBook material) {
        // TODO T4: implement the documented extension contract.
        Objects.requireNonNull(material);
        long weight = material.weightGrams();
        long startedHundreds = (weight + 99) / 100;
        long cents = startedHundreds * 50;
        return cents;
    }
 
    /** {@inheritDoc} */
    @Override
    public Long visit(Magazine material) {
        Objects.requireNonNull(material);
        return 200L;
    }
 
    /** {@inheritDoc} */
    @Override
    public Long visit(AudioBook material) {
        return Objects.requireNonNull(material).delivery() == Delivery.PHYSICAL ? 50L : 0L;
    }
 
    /** {@inheritDoc} */
    @Override
    public Long visit(VideoMaterial material) {
        // TODO T4: implement the documented extension contract.
        return Objects.requireNonNull(material).delivery() == Delivery.PHYSICAL ? 100L : 0L;
    }
 
    /** {@inheritDoc} */
    @Override
    public Long visit(EBook material) {
        Objects.requireNonNull(material);
        return 0L;
    }
}
 
