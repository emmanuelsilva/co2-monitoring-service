package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.repository.ActiveBySensorFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        return SensorAlert.create(sensor, now);
    }

    @Override
    SensorAlert getInactiveMock() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        return SensorAlert.create(sensor, now, now);
    }

    @BeforeEach
    public void setUp() {
        this.repository = new InMemorySensorAlertRepository();
    }

    @Test
    void shouldFindAllAlerts() {
        var sensor = new Sensor("123");
        var quantity = 5;
        saveListOfAlertsOnDatabase(sensor, quantity);

        var alerts = this.repository.findAllBySensorId(sensor.getId());
        assertEquals(quantity, alerts.size());
    }

    private void saveListOfAlertsOnDatabase(Sensor sensor, int quantity) {
        IntStream.rangeClosed(1, quantity).forEach(i -> {
            var now = OffsetDateTime.now();
            var alert = SensorAlert.create(sensor, now);
            this.repository.save(alert);
        });
    }

}