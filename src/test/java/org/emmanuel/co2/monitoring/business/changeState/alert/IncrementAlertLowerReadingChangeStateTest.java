package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IncrementAlertLowerReadingChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenSensorIsAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var incrementAlertLowerReadingChangeState = new IncrementAlertLowerReadingChangeState();
        var match = incrementAlertLowerReadingChangeState.rule().match(alertState, lowerThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldIncrementLowerReading() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerThresholdMeasurement = super.getLowerThresholdMeasurement(sensor);

        var incrementAlertLowerReadingChangeState = new IncrementAlertLowerReadingChangeState();
        var newState = incrementAlertLowerReadingChangeState.changeState(alertState, lowerThresholdMeasurement);

        assertThatAlertLowerCounterWasIncremented(newState);
    }
}