package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class RegisterSensorMeasurementTest {

    private RegisterSensorMeasurement registerSensorMeasurement;
    private SensorMeasurementRepository sensorMeasurementRepository;

    @BeforeEach
    void setUp() {
        this.sensorMeasurementRepository = mock(SensorMeasurementRepository.class);
        this.registerSensorMeasurement = new RegisterSensorMeasurement(this.sensorMeasurementRepository);
    }

    @Test
    void shouldRegisterValidMeasurement() {
        var sensor = new Sensor("123");
        var measurement = new SensorMeasurement(sensor, 10, OffsetDateTime.now());
        this.registerSensorMeasurement.register(measurement);

        assertThatMeasurementWasSaved(measurement);
    }

    @Test
    void shouldFailWhenThereIsNoSensor() {
        var measurement = new SensorMeasurement(null, 10, OffsetDateTime.now());
        assertThrows(IllegalArgumentException.class, () -> this.registerSensorMeasurement.register(measurement));
        assertThatThereWasNoRepositoryInteraction();
    }

    @Test
    void shouldFailWhenThereIsNoTimestamp() {
        var sensor = new Sensor("123");
        var measurement = new SensorMeasurement(sensor, 10, null);
        assertThrows(IllegalArgumentException.class, () -> this.registerSensorMeasurement.register(measurement));
        assertThatThereWasNoRepositoryInteraction();
    }

    private void assertThatMeasurementWasSaved(SensorMeasurement measurement) {
        verify(this.sensorMeasurementRepository).save(measurement);
    }

    private void assertThatThereWasNoRepositoryInteraction() {
        verifyNoInteractions(this.sensorMeasurementRepository);
    }
}