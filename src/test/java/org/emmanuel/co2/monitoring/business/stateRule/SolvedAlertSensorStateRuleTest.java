package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolvedAlertSensorStateRuleTest {

    private SolvedAlertSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new SolvedAlertSensorStateRule();
    }

    @Test
    void shouldAcceptAlertStateAndLowerMeasurement() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);

        var alert = SensorAlert.from(warning);
        alert.addLowerRead(new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now));
        alert.addLowerRead(new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 200, now));

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
        var lowerMeasurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var accepted = this.rule.accept(alertState, lowerMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldChangeStateToOK() {

        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);

        var alert = SensorAlert.from(warning);
        alert.addLowerRead(new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now));
        alert.addLowerRead(new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 200, now));

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
        var lowerMeasurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var newState = this.rule.defineState(alertState, lowerMeasurement);

        assertNotNull(newState);
        assertEquals(SensorState.OK, newState.getState());

        var alertOpt = newState.getAlert();
        assertTrue(alertOpt.isPresent());
        assertNotNull(alertOpt.get().getEndAt());
    }
}