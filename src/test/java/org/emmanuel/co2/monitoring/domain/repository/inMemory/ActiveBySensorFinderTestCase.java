package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.HasSensor;
import org.emmanuel.co2.monitoring.domain.repository.ActiveBySensorFinder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class ActiveBySensorFinderTestCase<T extends HasSensor> extends InMemoryRepositoryTestCase<T> {

    abstract ActiveBySensorFinder<T> getActiveFinder();

    abstract T getActiveMock();

    abstract T getInactiveMock();

    @Test
    void shouldNotReturnActiveSensorWhenThereIsNoActive() {
        T inactiveEntity = super.saveToRepository(getInactiveMock());
        var noActiveResultOpt = getActiveFinder().findActiveBySensorId(inactiveEntity.getSensor().getId());
        assertFalse(noActiveResultOpt.isPresent());
    }

    @Test
    void shouldFindActiveBySensorId() {
        T activeEntity = super.saveToRepository(getActiveMock());
        var activeResultOpt = getActiveFinder().findActiveBySensorId(activeEntity.getSensor().getId());

        assertTrue(activeResultOpt.isPresent());
        assertEquals(activeEntity, activeResultOpt.get());
    }

}
