package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IncrementLowerAlertReadingSensorStateRuleTest {

    private IncrementLowerAlertReadingSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new IncrementLowerAlertReadingSensorStateRule();
    }

    @Test
    void shouldAcceptAlertStateAndLowerMeasurement() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);
        var alert = SensorAlert.from(warning);

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
        var lowerMeasurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var accepted = this.rule.accept(alertState, lowerMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldIncrementLowerReads() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);
        var alert = SensorAlert.from(warning);

        var currentSensorLowerReadsCount = alert.getLowerReads().size();

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
        var lowerMeasurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var newState = this.rule.defineState(alertState, lowerMeasurement);
        var newAlertOpt = newState.getAlert();

        assertTrue(newAlertOpt.isPresent());
        assertEquals(SensorState.ALERT, newState.getState());
        assertEquals(currentSensorLowerReadsCount + 1, newAlertOpt.get().getLowerReads().size());
    }
}