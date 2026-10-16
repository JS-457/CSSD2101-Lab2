package edu.cssd2101.lab02;

/** Useful baseline demonstration; extension exercises are listed in the handout. */
public final class Main {
    private Main() {}

    /**
     * Demonstrates existing polymorphism and baseline catalogue behavior.
     *
     * @param args ignored arguments
     */
    public static void main(String[] args) {
        MaterialCatalog catalog = new InMemoryCatalog();
        catalog.add(
                MaterialFactory.printed(
                        new CatalogData("P1", "Java Design", "Ada", 2000, 2022), 501));
        catalog.add(
                MaterialFactory.magazine(
                        new CatalogData("M1", "Object Monthly", "Grace", 800, 2025), 12));
        for (Material m : catalog.all())
            System.out.println(m.id() + " price=" + m.priceCents(2026, DiscountStrategy.none()));
        System.out.println("catalogue size=" + catalog.all().size());
    }
}
