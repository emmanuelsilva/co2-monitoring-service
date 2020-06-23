package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.repository.ActiveBySensorFinder;
import org.junit.jupiter.api.BeforeEach;

import java.time.OffsetDateTime;
import java.util.Collections;

class InMemorySensorWarningRepositoryTest extends ActiveBySensorFinderTestCase<SensorWarning> {

    private InMemorySensorWarningRepository repository;

    @Override
    ActiveBySensorFinder<SensorWarning> getActiveFinder() {
        return this.repository;
    }

    @Override
    InMemoryRepository<SensorWarning> getRepository() {
        return this.repository;
    }

    @Override
    SensorWarning getActiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return new SensorWarning(sensor, now, null, Collections.emptyList());
    }

    @Override
    SensorWarning getInactiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return new SensorWarning(sensor, now, now, Collections.emptyList());
    }

    @BeforeEach
    public void setUp() {
        this.repository = new InMemorySensorWarningRepository();
    }
}