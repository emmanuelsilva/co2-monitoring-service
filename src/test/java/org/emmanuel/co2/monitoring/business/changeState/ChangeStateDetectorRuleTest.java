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

        var rule = ChangeStateDetectorRule.start()
                .whenStateIs(SensorState.OK);

        assertTrue(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldNotMatchWhenStateIsDifferentFromExpected() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = ChangeStateDetectorRule.start()
                .whenStateIs(SensorState.WARN);

        assertFalse(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldMatchWhenMeasurementIsAbove() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = ChangeStateDetectorRule.start()
                .measurementIsAbove(SensorThresholdConfiguration.THRESHOLD.value());

        assertTrue(rule.match(okState, higherMeasurement));
    }

    @Test
    void shouldMatchWhenMeasurementIsBelow() {
        var sensor  = givenSensor();
        var okState = givenOKState(sensor);
        var lowerThresholdMeasurement = getLowerThresholdMeasurement(sensor);

        var rule = ChangeStateDetectorRule.start()
                .measurementIsBelow(SensorThresholdConfiguration.THRESHOLD.value());

        assertTrue(rule.match(okState, lowerThresholdMeasurement));
    }

    @Test
    void shouldMatchWhenStateHasWarningAttempts() {
        var sensor  = givenSensor();
        var warnState = givenWarningSateWithMaxAttempts(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var rule = ChangeStateDetectorRule.start()
                .withWarningAttempts(2);

        assertTrue(rule.match(warnState, higherMeasurement));
    }

    @Test
    void withAlertLowerAttempts() {
        var sensor  = givenSensor();
        var warnState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var rule = ChangeStateDetectorRule.start()
                .withAlertLowerAttempts(2);

        assertTrue(rule.match(warnState, lowerMeasurement));
    }
}