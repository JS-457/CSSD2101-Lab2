package edu.cssd2101.lab02;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/** One LinkedHashMap owns ordering and uniqueness; expected O(1) ID operations, O(n) snapshots. */
public final class InMemoryCatalog implements MaterialCatalog {
    private final LinkedHashMap<String, Material> entries = new LinkedHashMap<>();

    /** Creates an empty unsynchronized catalogue. */
    public InMemoryCatalog() {}

    /** {@inheritDoc} */
    @Override
    public boolean add(Material material) {
        Objects.requireNonNull(material);
        return entries.putIfAbsent(material.id(), material) == null;
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Material> remove(String id) {
        return Optional.ofNullable(entries.remove(CatalogData.text(id)));
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Material> find(String id) {
        return Optional.ofNullable(entries.get(CatalogData.text(id)));
    }

    /** {@inheritDoc} */
    @Override
    public List<Material> all() {
        return List.copyOf(entries.values());
    }
}
