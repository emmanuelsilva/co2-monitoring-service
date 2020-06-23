package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ComputeCurrentSensorStateTest {

    @Test
    void shouldReturnOKWhenThereIsNoWarning() {
        var sensor = new Sensor("123");

        ComputeCurrentSensorState computeCurrentSensorState = new ComputeCurrentSensorState();
        var currentState = computeCurrentSensorState.compute(sensor, null);

        assertNotNull(currentState);
        assertEquals(SensorState.OK, currentState.getState());
    }

    @Test
    void shouldReturnWarningWhenThereIsOpenWarning() {
        var sensor = new Sensor("123");
        var openWarning = SensorWarning.create(sensor, OffsetDateTime.now());

        ComputeCurrentSensorState computeCurrentSensorState = new ComputeCurrentSensorState();
        var currentState = computeCurrentSensorState.compute(sensor, openWarning);

        assertNotNull(currentState);
        assertEquals(SensorState.WARN, currentState.getState());
    }

}