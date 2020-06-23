package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Simple In-memory database. It was created to running application without taking care of connection database and
 * let the infrastructure as simple as possible to put the focus on finishing the business rules.
 */
public abstract class InMemoryRepository<T> {

    private Set<T> records;

    protected InMemoryRepository() {
        this.records = new HashSet<>();
    }

    public T save(T record) {
        this.records.removeIf(r -> r.equals(record));
        this.records.add(record);
        return record;
    }

    public Optional<T> find(Predicate<T> filter) {
        return this.records.stream().filter(filter).findFirst();
    }

    public List<T> findAll(Predicate<T> filter) {
        return this.records.stream().filter(filter).collect(Collectors.toList());
    }

    public List<T> findAll() {
        return new ArrayList<>(this.records);
    }

}
