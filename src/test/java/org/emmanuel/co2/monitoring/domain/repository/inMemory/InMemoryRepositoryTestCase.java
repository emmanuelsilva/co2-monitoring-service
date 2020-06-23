package org.emmanuel.co2.monitoring.domain.repository.inMemory;

abstract class InMemoryRepositoryTestCase<T> {

    abstract InMemoryRepository<T> getRepository();

    protected T saveToRepository(T mock) {
        return getRepository().save(mock);
    }
}
