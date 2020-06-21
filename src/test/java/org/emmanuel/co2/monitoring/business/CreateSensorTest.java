package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThrows(IllegalArgumentException.class, () -> this.createSensor.getOrCreate(null));
        assertThatThereWasNoRepositoryInteraction();
    }

    @Test
    void shouldCreateValidSensor() {
        var id = "12345";
        givenNonExistentSensorOnRepositoryBehavior(id);
        var sensor = this.createSensor.getOrCreate(id);

        assertNotNull(sensor);
        assertThatTriedToFindSensorOnDatabase(id);
        assertThatSensorWasSaved(sensor);
    }

    @Test
    void shouldDoNotSaveAlreadyCreatedSensor() {
        var id = "12345";
        givenExistentSensorOnRepositoryBehavior(id);
        var sensor = this.createSensor.getOrCreate(id);

        assertThatTriedToFindSensorOnDatabase(id);
        assertThatSensorWasNotSaved(sensor);
    }

    private void assertThatTriedToFindSensorOnDatabase(String id) {
        verify(this.sensorRepository).findById(id);
    }

    private void givenNonExistentSensorOnRepositoryBehavior(String id) {
        var sensor = new Sensor(id);

        when(this.sensorRepository.findById(id)).thenReturn(Optional.empty());
        when(this.sensorRepository.save(sensor)).thenReturn(sensor);
    }

    private void givenExistentSensorOnRepositoryBehavior(String id) {
        when(this.sensorRepository.findById(id)).thenReturn(Optional.of(new Sensor(id)));
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