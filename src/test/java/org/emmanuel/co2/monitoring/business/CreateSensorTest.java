package org.emmanuel.co2.monitoring.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateSensorTest {

    private CreateSensor createSensor;

    @BeforeEach
    void setUp() {
        this.createSensor = new CreateSensor();
    }

    @Test
    void shouldFailWhenThereIsNoId() {
        assertThrows(IllegalArgumentException.class, () -> this.createSensor.create(null));
    }

    @Test
    void shouldCreateValidSensor() {
        var id = "12345";
        var sensor = this.createSensor.create(id);

        assertNotNull(sensor);
        assertEquals(id, sensor.getId());
    }
}