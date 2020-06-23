package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolvedWarningDetectorSensorStateRuleTest {

    private SolvedWarningDetectorSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new SolvedWarningDetectorSensorStateRule();
    }

    @Test
    void shouldAcceptWarningStateAndLowerMeasurement() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);
        var warnState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        var measurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var accepted = this.rule.accept(warnState, measurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenIsNotWarning() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var okState = new CurrentSensorState(sensor, SensorState.OK);
        var measurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var accepted = this.rule.accept(okState, measurement);
        assertFalse(accepted);
    }

    @Test
    void shouldNotAcceptWhenIsMeasurementIsHigherThanThreshold() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);
        var warnState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        var measurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() + 100, now);

        var accepted = this.rule.accept(warnState, measurement);
        assertFalse(accepted);
    }

    @Test
    void shouldChangeStateToOKWhenCurrentStateIsWarnAndMeasurementIsLowerThanThreshold() {
        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);
        var warnState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        var measurement = new SensorMeasurement(sensor, SensorThresholdConfiguration.THRESHOLD.value() - 100, now);

        var newState = this.rule.defineState(warnState, measurement);

        assertStateWasChangedToOK(newState);
    }

    private void assertStateWasChangedToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.OK, newState.getState());

        var newWarningOpt = newState.getWarning();
        assertTrue(newWarningOpt.isPresent());
        assertFalse(newWarningOpt.get().isOpened());
    }
}