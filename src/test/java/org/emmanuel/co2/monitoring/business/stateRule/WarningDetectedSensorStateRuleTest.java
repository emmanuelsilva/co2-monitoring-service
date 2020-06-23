package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class WarningDetectedSensorStateRuleTest {

    private WarningDetectedSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new WarningDetectedSensorStateRule();
    }

    @Test
    void shouldNotAcceptWhenSensorStateIsNotOK() {
        var sensor = new Sensor("123");
        var warnState = new CurrentSensorState(sensor, SensorState.WARN);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldNotAcceptWhenMeasurementIsLowerThanThreshold() {
        var sensor = new Sensor("123");
        var okState = new CurrentSensorState(sensor, SensorState.OK);
        var nonWarningMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = rule.accept(okState, nonWarningMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldAcceptWhenMeasurementIsHigherThanThresholdAndSensorStateIsOK() {
        var sensor = new Sensor("123");
        var okState = new CurrentSensorState(sensor, SensorState.OK);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(okState, warningMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldChangeStateToWarnWhenMeasurementIsHigherThantThresholdAndSensorStateIsOK() {
        var sensor = new Sensor("123");
        var okState = new CurrentSensorState(sensor, SensorState.OK);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);
        var newState = rule.defineState(okState, warningMeasurement);

        assertThatWarningWasCreateCorrectly(warningMeasurement, newState);
    }

    private void assertThatWarningWasCreateCorrectly(SensorMeasurement warningMeasurement, CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());

        var warningOpt = newState.getWarning();

        assertTrue(warningOpt.isPresent());
        assertNull(warningOpt.get().getEndAt());
        assertEquals(1, warningOpt.get().getWarningReads().size());
        assertTrue(warningOpt.get().getWarningReads().contains(warningMeasurement.getValue()));
    }

    private SensorMeasurement getHigherThresholdMeasurement(Sensor sensor) {
        return new SensorMeasurement(
                    sensor,
                    SensorThresholdConfiguration.THRESHOLD.value() + 500,
                    OffsetDateTime.now());
    }

    private SensorMeasurement getLowerThresholdMeasurement(Sensor sensor) {
        return new SensorMeasurement(
                sensor,
                SensorThresholdConfiguration.THRESHOLD.value() - 100,
                OffsetDateTime.now());
    }
}