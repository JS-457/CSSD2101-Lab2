package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.List;

class ShippingCheckoutTest {
    private CatalogData data(String id, long cents) {
        return new CatalogData(id, "Title", "Ada", cents, 2026);
    }

    @Test
    void visitorUsesEveryConcreteOverloadAndHasNoAccumulatingState() {
        var s = new ShippingVisitor();
        for (int grams : new int[] {1, 99, 100, 101, 500, 501, Integer.MAX_VALUE}) {
            Material m = new PrintedBook(data("P", 0), grams);
            assertEquals(((grams + 99L) / 100L) * 50L, m.accept(s));
            assertEquals(m.accept(s), m.accept(s));
        }
        assertEquals(200L, new Magazine(data("M", 0), 1).accept(s));
        assertEquals(50L, new AudioBook(data("A", 0), 1, Delivery.PHYSICAL, false).accept(s));
        assertEquals(0L, new AudioBook(data("A", 0), 1, Delivery.DIGITAL, false).accept(s));
        assertEquals(
                100L, new VideoMaterial(data("V", 0), 1, Delivery.PHYSICAL, List.of()).accept(s));
        assertEquals(0L, new VideoMaterial(data("V", 0), 1, Delivery.DIGITAL, List.of()).accept(s));
        assertEquals(0L, new EBook(data("E", 0), EBookFormat.EPUB, 1, 0, false).accept(s));
        assertThrows(NullPointerException.class, () -> new Magazine(data("M", 0), 1).accept(null));
    }

    @Test
    void checkoutSumsRoundedLineItemsAndShippingWithoutMutatingCatalog() {
        MaterialCatalog c = new InMemoryCatalog();
        c.add(new PrintedBook(data("P", 101), 100));
        c.add(new EBook(data("E", 101), EBookFormat.EPUB, 1, 251, false));
        var checkout = new Checkout(DiscountStrategy.fixed(1000), new ShippingVisitor());
        assertEquals(218, checkout.totalCents(c, 2026)); // 91 + 50 + 77
        assertEquals(218, checkout.totalCents(c, 2026));
        assertEquals(2, c.all().size());
        assertEquals(0, checkout.totalCents(new InMemoryCatalog(), 2026));
        assertThrows(
                IllegalArgumentException.class,
                () -> checkout.totalCents(new InMemoryCatalog(), 0));
    }

    @Test
    void overflowAndInvalidPoliciesAreExplicitAndPreserveState() {
        MaterialCatalog c = new InMemoryCatalog();
        c.add(new PrintedBook(data("P", Long.MAX_VALUE), 1));
        var checkout = new Checkout(DiscountStrategy.none(), new ShippingVisitor());
        assertThrows(ArithmeticException.class, () -> checkout.totalCents(c, 2026));
        assertEquals(1, c.all().size());
        assertThrows(NullPointerException.class, () -> new Checkout(null, new ShippingVisitor()));
        assertThrows(NullPointerException.class, () -> new Checkout(DiscountStrategy.none(), null));
        assertThrows(NullPointerException.class, () -> checkout.totalCents(null, 2026));
        MaterialVisitor<Long> negative =
                new MaterialVisitor<>() {
                    public Long visit(PrintedBook m) {
                        return -1L;
                    }

                    public Long visit(Magazine m) {
                        return -1L;
                    }

                    public Long visit(AudioBook m) {
                        return -1L;
                    }

                    public Long visit(VideoMaterial m) {
                        return -1L;
                    }

                    public Long visit(EBook m) {
                        return -1L;
                    }
                };
        assertThrows(
                IllegalArgumentException.class,
                () -> new Checkout(DiscountStrategy.none(), negative).totalCents(c, 2026));
    }

    @Test
    void demonstrationRuns() {
        Main.main(new String[0]);
    }
}
