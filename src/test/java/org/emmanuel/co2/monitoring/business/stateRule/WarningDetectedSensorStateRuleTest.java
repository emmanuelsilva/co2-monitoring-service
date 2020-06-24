package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarningDetectedSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private WarningDetectedSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new WarningDetectedSensorStateRule();
    }

    @Test
    void shouldNotAcceptWhenSensorStateIsNotOK() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, higherMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldAcceptWhenMeasurementIsHigherThanThresholdAndSensorStateIsOK() {
        var sensor = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(okState, higherMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldChangeStateToWarn() {
        var sensor = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);
        var newState = rule.defineState(okState, higherMeasurement);

        assertThatWarningWasCreated(higherMeasurement, newState);
    }

    private void assertThatWarningWasCreated(SensorMeasurement warningMeasurement, CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());

        var warningOpt = newState.getWarning();
        assertTrue(warningOpt.isPresent());

        var warning = warningOpt.get();
        assertNull(warning.getEndAt());
        assertEquals(1, warning.getHigherReads().size());
        assertTrue(warning.getHigherReads().contains(warningMeasurement.getValue()));
    }
}