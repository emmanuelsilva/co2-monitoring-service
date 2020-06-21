package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultSensorMeasurementServiceTest {

    private DefaultSensorMeasurementService service;
    private SensorRepository sensorRepository;
    private SensorMeasurementRepository sensorMeasurementRepository;

    @BeforeEach
    void setUp() {
        this.sensorRepository = mock(SensorRepository.class);
        this.sensorMeasurementRepository = mock(SensorMeasurementRepository.class);
        this.service = new DefaultSensorMeasurementService(sensorRepository, sensorMeasurementRepository);
    }

    @Test
    void shouldMeasureDataFromNonSavedSensor() {
        SensorMeasurementRequest request = buildValidRequest();
        var sensor = new Sensor(request.getSensorId());
        var sensorMeasurement = new SensorMeasurement(sensor, request.getValue(), request.getTime());

        givenNonExistentSensorOnRepository(request);
        givenSavedSensorSensor(sensor);
        givenSavedSensorMeasurement(sensorMeasurement);

        var result = this.service.measure(request);

        assertNotNull(result);
        assertThatTriedToFindSensorOnDatabase(request);
        assertThatSavedSensorOnDatabase(sensor);
        assertThatSavedMeasurementOnDatabase(sensorMeasurement);
        assertThatWasCreatedMeasurementAsRequestedValues(request, result);
    }

    @Test
    void shouldMeasureDataFromAlreadySavedSensor() {
        SensorMeasurementRequest request = buildValidRequest();
        var sensor = new Sensor(request.getSensorId());
        var sensorMeasurement = new SensorMeasurement(sensor, request.getValue(), request.getTime());

        givenExistentSensorOnRepository(sensor);
        givenSavedSensorMeasurement(sensorMeasurement);

        var result = this.service.measure(request);

        assertNotNull(result);
        assertThatTriedToFindSensorOnDatabase(request);
        assertThatSavedSensorWasNotSaved(sensor);
        assertThatSavedMeasurementOnDatabase(sensorMeasurement);
        assertThatWasCreatedMeasurementAsRequestedValues(request, result);
    }

    @Test
    void shouldFailErrorWhenThereIsNoSensorId() {
        var request = buildWithoutSensorIdRequest();
        assertThrows(IllegalArgumentException.class, () -> service.measure(request));
    }

    @Test
    void shouldFailErrorWhenThereIsNoTimestamp() {
        var request = buildWithoutTimeRequest();
        assertThrows(IllegalArgumentException.class, () -> service.measure(request));
    }

    private SensorMeasurementRequest buildValidRequest() {
        return SensorMeasurementRequest.builder()
                    .sensorId("12345")
                    .time(OffsetDateTime.now())
                    .value(100)
                    .build();
    }

    private SensorMeasurementRequest buildWithoutSensorIdRequest() {
        return SensorMeasurementRequest.builder()
                .sensorId(null)
                .time(OffsetDateTime.now())
                .value(100)
                .build();
    }

    private SensorMeasurementRequest buildWithoutTimeRequest() {
        return SensorMeasurementRequest.builder()
                .sensorId("12345")
                .time(null)
                .value(100)
                .build();
    }

    private void assertThatWasCreatedMeasurementAsRequestedValues(SensorMeasurementRequest request, SensorMeasurement result) {
        assertEquals(request.getSensorId(), result.getSensor().getId());
        assertEquals(request.getTime(), result.getTimestamp());
        assertEquals(request.getValue(), result.getValue());
    }

    private void assertThatSavedMeasurementOnDatabase(SensorMeasurement sensorMeasurement) {
        verify(this.sensorMeasurementRepository).save(sensorMeasurement);
    }

    private void assertThatSavedSensorOnDatabase(Sensor sensor) {
        verify(this.sensorRepository).save(sensor);
    }

    private void assertThatSavedSensorWasNotSaved(Sensor sensor) {
        verify(this.sensorRepository, never()).save(sensor);
    }

    private void assertThatTriedToFindSensorOnDatabase(SensorMeasurementRequest request) {
        verify(this.sensorRepository).findById(request.getSensorId());
    }

    private void givenSavedSensorMeasurement(SensorMeasurement sensorMeasurement) {
        when(this.sensorMeasurementRepository.save(sensorMeasurement)).thenReturn(sensorMeasurement);
    }

    private void givenSavedSensorSensor(Sensor sensor) {
        when(this.sensorRepository.save(sensor)).thenReturn(sensor);
    }

    private void givenNonExistentSensorOnRepository(SensorMeasurementRequest request) {
        when(this.sensorRepository.findById(request.getSensorId())).thenReturn(Optional.empty());
    }

    private void givenExistentSensorOnRepository(Sensor sensor) {
        when(this.sensorRepository.findById(sensor.getId())).thenReturn(Optional.of(sensor));
    }
}