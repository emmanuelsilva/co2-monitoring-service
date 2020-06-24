package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SolvedAlertSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private SolvedAlertSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new SolvedAlertSensorStateRule();
    }

    @Test
    void shouldAcceptAlertStateAndLowerMeasurement() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = this.rule.accept(alertState, lowerMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenNotReachLowerMaxAttempts() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = this.rule.accept(alertState, lowerMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldChangeStateToOK() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerMeasurement = getLowerThresholdMeasurement(sensor);

        var newState = this.rule.defineState(alertState, lowerMeasurement);

        assertThatStateWasChangeToOK(newState);
    }

    private void assertThatStateWasChangeToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.OK, newState.getState());

        var alertOpt = newState.getAlert();
        assertTrue(alertOpt.isPresent());
        assertNotNull(alertOpt.get().getEndAt());
    }
}