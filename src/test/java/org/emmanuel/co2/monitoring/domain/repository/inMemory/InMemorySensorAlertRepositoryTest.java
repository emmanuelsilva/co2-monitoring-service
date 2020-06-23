package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.repository.ActiveBySensorFinder;
import org.junit.jupiter.api.BeforeEach;

import java.time.OffsetDateTime;
import java.util.Collections;

class InMemorySensorAlertRepositoryTest extends ActiveBySensorFinderTestCase<SensorAlert> {

    private InMemorySensorAlertRepository repository;

    @Override
    ActiveBySensorFinder<SensorAlert> getActiveFinder() {
        return this.repository;
    }

    @Override
    InMemoryRepository<SensorAlert> getRepository() {
        return this.repository;
    }

    @Override
    SensorAlert getActiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return new SensorAlert(sensor, now, null, Collections.emptyList());
    }

    @Override
    SensorAlert getInactiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return new SensorAlert(sensor, now, now, Collections.emptyList());
    }

    @BeforeEach
    public void setUp() {
        this.repository = new InMemorySensorAlertRepository();
    }


}