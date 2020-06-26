package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenSensorStateIsAlertReachMaxWarningReads() {
        var sensor = givenSensor();
        var warningState = super.givenWarningSateWithMaxAttempts(sensor);
        var higherThresholdMeasurement = super.getHigherThresholdMeasurement(sensor);

        var alertChangeState = new AlertChangeState();
        var match = alertChangeState.rule().match(warningState, higherThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldChangeStateToAlert() {
        var sensor = givenSensor();
        var warningState = super.givenWarningSateWithMaxAttempts(sensor);
        var higherThresholdMeasurement = super.getHigherThresholdMeasurement(sensor);

        var alertChangeState = new AlertChangeState();
        var newState = alertChangeState.changeState(warningState, higherThresholdMeasurement);

        assertThatSensorStatusWasChangedToAlert(higherThresholdMeasurement, newState);
    }
}