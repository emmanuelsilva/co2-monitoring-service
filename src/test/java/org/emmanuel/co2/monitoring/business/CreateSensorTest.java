package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.Sensor;
import org.emmanuel.co2.monitoring.repository.SensorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateSensorTest {

    private CreateSensor createSensor;
    private SensorRepository sensorRepository;

    @BeforeEach
    void setUp() {
        this.sensorRepository = mock(SensorRepository.class);
        this.createSensor = new CreateSensor(this.sensorRepository);
    }

    @Test
    void shouldFailWhenThereIsNoId() {
        var sensor = new Sensor(null);
        assertThrows(IllegalArgumentException.class, () -> this.createSensor.create(sensor));
        assertThatThereWasNoRepositoryInteraction();
    }

    @Test
    void shouldCreateValidSensor() {
        var sensor = new Sensor("12345");
        givenNonExistentSensorOnRepositoryBehavior(sensor);
        this.createSensor.create(sensor);

        assertThatTriedToFindSensorOnDatabase(sensor);
        assertThatSensorWasSaved(sensor);
    }

    @Test
    void shouldDoNotSaveAlreadyCreatedSensor() {
        var sensor = new Sensor("12345");
        givenExistentSensorOnRepositoryBehavior(sensor);
        this.createSensor.create(sensor);

        assertThatTriedToFindSensorOnDatabase(sensor);
        assertThatSensorWasNotSaved(sensor);
    }

    private void assertThatTriedToFindSensorOnDatabase(Sensor sensor) {
        verify(this.sensorRepository).findById(sensor.getId());
    }

    private void givenNonExistentSensorOnRepositoryBehavior(Sensor sensor) {
        when(this.sensorRepository.findById(sensor.getId())).thenReturn(Optional.empty());
    }

    private void givenExistentSensorOnRepositoryBehavior(Sensor sensor) {
        when(this.sensorRepository.findById(sensor.getId())).thenReturn(Optional.of(sensor));
    }

    private void assertThatSensorWasSaved(Sensor sensor) {
        verify(this.sensorRepository).save(sensor);
    }

    private void assertThatSensorWasNotSaved(Sensor sensor) {
        verify(this.sensorRepository, never()).save(sensor);
    }

    private void assertThatThereWasNoRepositoryInteraction() {
        verifyNoInteractions(this.sensorRepository);
    }
}