package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SolveAlertChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenStateIsAlertAndReachMaxLowerAttempts() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var solveAlertChangeState = new SolveAlertChangeState();
        var match = solveAlertChangeState.rule().match(alertState, lowerThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldChangeToOKState() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var solveAlertChangeState = new SolveAlertChangeState();
        var newState = solveAlertChangeState.changeState(alertState, lowerThresholdMeasurement);

        assertThatSensorStatusWasChangedFromAlertToOK(newState);
    }
}