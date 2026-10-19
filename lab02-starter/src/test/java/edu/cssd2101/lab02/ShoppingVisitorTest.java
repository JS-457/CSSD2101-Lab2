package edu.cssd2101.lab02;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class ShippingVisitorTest {
    private final ShippingVisitor visitor = new ShippingVisitor();

    private static CatalogData data(String id) {
        return new CatalogData(id, "Title", "Ada", 1000, 2026);
    }

    @Test
    void printedBookChargesFiftyCentsPerStartedHundredGrams() {
        assertEquals(50L, visitor.visit(new PrintedBook(data("P1"), 1)));
        assertEquals(50L, visitor.visit(new PrintedBook(data("P2"), 99)));
        assertEquals(50L, visitor.visit(new PrintedBook(data("P3"), 100)));
        assertEquals(100L, visitor.visit(new PrintedBook(data("P4"), 101)));
        assertEquals(100L, visitor.visit(new PrintedBook(data("P5"), 200)));
        assertEquals(150L, visitor.visit(new PrintedBook(data("P6"), 201)));
    }

    @Test
    void printedBookMaximumWeightDoesNotOverflow() {
        long cents = visitor.visit(new PrintedBook(data("P1"), Integer.MAX_VALUE));
        assertEquals(1073741850L, cents);
    }

    @Test
    void magazineIsFlatTwoHundredCents() {
        assertEquals(200L, visitor.visit(new Magazine(data("M1"), 1)));
    }

    @Test
    void audioBookDependsOnDelivery() {
        AudioBook physical = new AudioBook(data("A1"), 60, Delivery.PHYSICAL, false);
        AudioBook digital = new AudioBook(data("A2"), 60, Delivery.DIGITAL, false);
        assertEquals(50L, visitor.visit(physical));
        assertEquals(0L, visitor.visit(digital));
    }

    @Test
    void videoDependsOnDeliveryNotRuntime() {
        VideoMaterial physical = new VideoMaterial(data("V1"), 90, Delivery.PHYSICAL, List.of("Sam"));
        VideoMaterial digital = new VideoMaterial(data("V2"), 5000, Delivery.DIGITAL, List.of("Sam"));
        assertEquals(100L, visitor.visit(physical));
        assertEquals(0L, visitor.visit(digital));
    }

    @Test
    void ebookIsAlwaysZero() {
        EBook book = MaterialFactory.ebook(data("E1"), 1024, 251).build();
        assertEquals(0L, visitor.visit(book));
    }

    @Test
    void repeatedCallsReturnTheSameCharge() {
        PrintedBook book = new PrintedBook(data("P1"), 101);
        assertEquals(100L, visitor.visit(book));
        assertEquals(100L, visitor.visit(book));
    }

    @Test
    void acceptOnMaterialReferenceSelectsTheCorrectOverload() {
        Material printed = new PrintedBook(data("P1"), 101);
        Material video = new VideoMaterial(data("V1"), 90, Delivery.PHYSICAL, List.of("Sam"));
        Material magazine = new Magazine(data("M1"), 1);
        assertEquals(100L, printed.accept(visitor));
        assertEquals(100L, video.accept(visitor));
        assertEquals(200L, magazine.accept(visitor));
    }

    @Test
    void nullMaterialIsRejected() {
        assertThrows(NullPointerException.class, () -> visitor.visit((PrintedBook) null));
        assertThrows(NullPointerException.class, () -> visitor.visit((VideoMaterial) null));
    }
}
