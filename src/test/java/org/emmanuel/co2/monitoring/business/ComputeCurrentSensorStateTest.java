package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;

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

    @Test
    void shouldReturnAlertWhenThereIsOpenAlert() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();

        var warning = new SensorWarning(sensor, now, now, Collections.emptyList());
        var alert = SensorAlert.from(warning);

        ComputeCurrentSensorState computeCurrentSensorState = new ComputeCurrentSensorState();
        var currentState = computeCurrentSensorState.compute(sensor, warning, alert);

        assertNotNull(currentState);
        assertEquals(SensorState.ALERT, currentState.getState());
    }

}