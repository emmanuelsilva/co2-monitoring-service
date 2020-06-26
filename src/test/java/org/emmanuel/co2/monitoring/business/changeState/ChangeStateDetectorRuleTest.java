package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChangeStateDetectorRuleTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenStateIsOK() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.OK)
                .build();

        assertTrue(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldNotMatchWhenStateIsDifferentFromExpected() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.WARN)
                .build();

        assertFalse(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldMatchWhenMeasurementIsAbove() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .measurementIsAbove(SensorThresholdConfiguration.THRESHOLD.value())
                .build();

        assertTrue(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldMatchWhenMeasurementIsBelow() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var lowerThresholdMeasurement = getLowerThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .measurementIsBelow(SensorThresholdConfiguration.THRESHOLD.value())
                .build();

        assertTrue(rule.match(okState, lowerThresholdMeasurement));
    }

    @Test
    void shouldMatchWhenStateHasWarningAttempts() {
        var sensor  = givenSensor();
        var warnState = givenWarningSateWithMaxAttempts(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .withWarningAttempts(2)
                .build();

        assertTrue(rule.match(warnState, higherMeasurement));
    }

    @Test
    void withAlertLowerAttempts() {
        var sensor  = givenSensor();
        var warnState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var rule = new ChangeStateDetectorRule.Builder()
                .withAlertLowerAttempts(2)
                .build();

        assertTrue(rule.match(warnState, lowerMeasurement));
    }
}