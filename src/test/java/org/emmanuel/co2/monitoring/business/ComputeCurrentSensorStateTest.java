package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

        var warning = SensorWarning.create(sensor, now, now);
        var alert = SensorAlert.from(warning);

        ComputeCurrentSensorState computeCurrentSensorState = new ComputeCurrentSensorState();
        var currentState = computeCurrentSensorState.compute(sensor, warning, alert);

        assertNotNull(currentState);
        assertEquals(SensorState.ALERT, currentState.getState());
    }

}