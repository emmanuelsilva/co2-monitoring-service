package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class InMemorySensorMeasurementRepositoryTest {

    private InMemorySensorMeasurementRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemorySensorMeasurementRepository();
    }

    @Test
    void shouldSave() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var sensorMeasurement = new SensorMeasurement(sensor, 100, now);

        var saved = repository.save(sensorMeasurement);
        assertNotNull(saved);
        assertEquals(sensorMeasurement, saved);
        assertTrue(repository.findAll().contains(saved));
    }

    @Test
    void shouldFilterMeasurementCreatedAfterMonthBegins() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();

        var nowMeasurement = new SensorMeasurement(sensor, 100, now);
        var lastMonthMeasurement = new SensorMeasurement(sensor, 50, now.minusMonths(1));

        this.repository.save(nowMeasurement);
        this.repository.save(lastMonthMeasurement);

        var beginOfMonth = now.withDayOfMonth(1);
        var measurements = repository
                .findAllMeasurementBySensorIdAndTimestampAfter(sensor.getId(), beginOfMonth);

        assertEquals(1, measurements.size());
        assertTrue(measurements.contains(nowMeasurement));
    }
}