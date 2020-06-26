package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IncrementWarningAttemptChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenSensorIsInAlertState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        IncrementWarningAttemptChangeState incrementWarningAttemptChangeState = new IncrementWarningAttemptChangeState();
        var match = incrementWarningAttemptChangeState.rule().match(warningState, higherThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldIncrementWarningAttempts() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        IncrementWarningAttemptChangeState incrementWarningAttemptChangeState = new IncrementWarningAttemptChangeState();
        var newState = incrementWarningAttemptChangeState.changeState(warningState, higherThresholdMeasurement);;

        var expectedIncrementedHighReads = warningState.getWarning().get().getHigherReads().size() + 1;
        assertThatWarningCounterWasIncremented(newState, expectedIncrementedHighReads);
    }
}