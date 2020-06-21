package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CreateSensorMeasurementTest {

    private CreateSensorMeasurement createSensorMeasurement;

    @BeforeEach
    void setUp() {
        this.createSensorMeasurement = new CreateSensorMeasurement();
    }

    @Test
    void shouldCreateValidMeasurement() {
        var sensor = new Sensor("123");
        var request = SensorMeasurementRequest.builder()
                .value(10)
                .time(OffsetDateTime.now())
                .build();

        var measurement = this.createSensorMeasurement.create(sensor, request);

        assertNotNull(measurement);
        assertThatMeasurementIsSameAsRequest(sensor, request, measurement);
    }

    @Test
    void shouldFailWhenThereIsNoSensor() {
        var request = SensorMeasurementRequest.builder()
                .value(10)
                .time(OffsetDateTime.now())
                .build();

        assertThrows(IllegalArgumentException.class, () -> this.createSensorMeasurement.create(null, request));
    }

    @Test
    void shouldFailWhenThereIsNoTimestamp() {
        var sensor = new Sensor("123");
        var request = SensorMeasurementRequest.builder()
                .value(10)
                .build();

        assertThrows(IllegalArgumentException.class, () -> this.createSensorMeasurement.create(sensor, request));
    }

    private void assertThatMeasurementIsSameAsRequest(Sensor sensor, SensorMeasurementRequest request, SensorMeasurement measurement) {
        assertEquals(sensor, measurement.getSensor());
        assertEquals(request.getValue(), measurement.getValue());
        assertEquals(request.getTime(), measurement.getTimestamp());
    }
}