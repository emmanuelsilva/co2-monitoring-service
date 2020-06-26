package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SolveWarningChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shoulMatchWhenSensorIsAlertState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var solveWarningChangeState = new SolveWarningChangeState();
        var match = solveWarningChangeState.rule().match(warningState, lowerThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldChangeToOKState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var solveWarningChangeState = new SolveWarningChangeState();
        var newState = solveWarningChangeState.changeState(warningState, lowerThresholdMeasurement);

        assertThatSensorStatusWasChangedFromWarnToOK(newState);
    }
}