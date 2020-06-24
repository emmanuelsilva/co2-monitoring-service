package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolvedWarningDetectorSensorStateRuleTest  extends BaseSensorStateRuleTestCase {

    private SolvedWarningDetectorSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new SolvedWarningDetectorSensorStateRule();
    }

    @Test
    void shouldAcceptWarningStateAndLowerMeasurement() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = this.rule.accept(warnState, lowerMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenIsNotWarning() {
        var sensor = givenSensor();
        var okState = givenOKState(givenSensor());
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = this.rule.accept(okState, lowerMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldNotAcceptWhenIsMeasurementIsHigherThanThreshold() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var lowerMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = this.rule.accept(warnState, lowerMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldChangeStateToOK() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var newState = this.rule.defineState(warnState, lowerMeasurement);

        assertStateWasChangedToOK(newState);
    }

    private void assertStateWasChangedToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.OK, newState.getState());

        var newWarningOpt = newState.getWarning();
        assertTrue(newWarningOpt.isPresent());

        var warning = newWarningOpt.get();
        assertFalse(warning.isOpened());
    }
}