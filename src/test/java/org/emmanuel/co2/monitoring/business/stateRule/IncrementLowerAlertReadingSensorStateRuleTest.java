package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncrementLowerAlertReadingSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private IncrementLowerAlertReadingSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new IncrementLowerAlertReadingSensorStateRule();
    }

    @Test
    void shouldAcceptWhenAlertStateAndLowerMeasurement() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = this.rule.accept(alertState, lowerMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenAlertStateAndHigherMeasurement() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = this.rule.accept(alertState, higherMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldIncrementLowerReads() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var currentSensorLowerReadsCount = alertState.getAlert().get().getLowerReads().size();

        var newState = this.rule.defineState(alertState, lowerMeasurement);
        assertThatLowerReadsWasIncremented(currentSensorLowerReadsCount, newState);
    }

    private void assertThatLowerReadsWasIncremented(int currentSensorLowerReadsCount, CurrentSensorState newState) {
        var newAlertOpt = newState.getAlert();
        assertTrue(newAlertOpt.isPresent());
        assertEquals(SensorState.ALERT, newState.getState());
        assertEquals(currentSensorLowerReadsCount + 1, newAlertOpt.get().getLowerReads().size());
    }

}