package edu.cssd2101.lab02;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Mutation and query contract for an insertion-ordered catalogue of immutable materials. Null
 * arguments and blank IDs are invalid. Duplicate IDs preserve the first instance. Query functions
 * must not mutate this catalogue. Implementations are not thread-safe.
 */
public interface MaterialCatalog {
    /**
     * Adds an absent ID.
     *
     * @param material nonnull entry
     * @return true if added
     * @throws NullPointerException if material is null
     */
    boolean add(Material material);

    /**
     * Removes by ID.
     *
     * @param id nonblank ID
     * @return removed entry or empty
     * @throws NullPointerException if id is null
     * @throws IllegalArgumentException if id is blank
     */
    Optional<Material> remove(String id);

    /**
     * Looks up an ID.
     *
     * @param id nonblank ID
     * @return matching entry or empty
     * @throws NullPointerException if id is null
     * @throws IllegalArgumentException if id is blank
     */
    Optional<Material> find(String id);

    /**
     * Returns an immutable detached snapshot.
     *
     * @return entries in insertion order
     */
    List<Material> all();

    /**
     * Selects a snapshot using a pure predicate in O(n).
     *
     * @param condition nonnull, non-mutating predicate
     * @return immutable matches in insertion order
     * @throws NullPointerException if condition is null
     */
    default List<Material> select(Predicate<Material> condition) {
        java.util.Objects.requireNonNull(condition);
        return all().stream().filter(condition).toList();
    }

    /**
     * Sorts a snapshot in O(n log n), preserving tie order.
     *
     * @param comparator nonnull comparator obeying its contract
     * @return immutable sorted result
     * @throws NullPointerException if comparator is null
     */
    default List<Material> sorted(Comparator<Material> comparator) {
        java.util.Objects.requireNonNull(comparator);
        return all().stream().sorted(comparator).toList();
    }

    /**
     * Finds a fixed number of calendar publication years ending at asOfYear.
     *
     * @param asOfYear context year 1450..9999
     * @param years positive count at most 10000; years=1 means just asOfYear
     * @return immutable results; future publications excluded
     * @throws IllegalArgumentException if the context or count is invalid
     */
    default List<Material> recent(int asOfYear, int years) {
        if (asOfYear > 9999 || asOfYear < 1450 || years > 9999 || years < 1){
            throw new IllegalArgumentException("Years out of range");
        }
        int first = asOfYear - years + 1;
        return select(m -> m.data().year() >= first && m.data().year() <= asOfYear);

    }

    /**
     * Matches exact creator names with OR semantics, normalized with Locale.ROOT.
     *
     * @param creators nonnull array of nonnull, nonblank names; empty matches nothing
     * @return immutable matches in insertion order without duplicate result rows
     * @throws NullPointerException if array or a name is null
     * @throws IllegalArgumentException if any name is blank
     */
    default List<Material> byCreators(String... creators) {
        java.util.Objects.requireNonNull(creators);
        var names = new java.util.HashSet<String>(); // Creates a new empty set
        for (String name : creators) // adding the creator names to a new list, before returning to the user.
            names.add(CatalogData.text(name).toLowerCase(java.util.Locale.ROOT));
        return select(m -> names.contains(m.data().creator().toLowerCase(java.util.Locale.ROOT)));
    }
}
