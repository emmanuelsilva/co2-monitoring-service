package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.repository.ActiveBySensorFinder;
import org.junit.jupiter.api.BeforeEach;

import java.time.OffsetDateTime;

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
        return SensorWarning.create(sensor, now);
    }

    @Override
    SensorWarning getInactiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return SensorWarning.create(sensor, now, now);
    }

    @BeforeEach
    public void setUp() {
        this.repository = new InMemorySensorWarningRepository();
    }
}