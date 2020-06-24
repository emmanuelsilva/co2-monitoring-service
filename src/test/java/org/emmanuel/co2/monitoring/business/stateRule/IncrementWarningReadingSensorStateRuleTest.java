package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncrementWarningReadingSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private IncrementWarningReadingSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new IncrementWarningReadingSensorStateRule();
    }

    @Test
    void shouldAcceptWhenStateIsWarningAndNotReachMaxWarnAttempts() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, higherMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenStateIsWarningButReachMaxWarnAttempts() {
        var sensor = givenSensor();
        var warnState = givenWarningSateWithMaxAttempts(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldIncrementWarningAttempts() {
        var sensor = givenSensor();
        var warnState = givenWarnState(sensor);
        var higherMeasurement = getHigherThresholdMeasurement(sensor);

        var result = rule.defineState(warnState, higherMeasurement);

        assertThatWarningWasIncremented(result);
    }

    private void assertThatWarningWasIncremented(CurrentSensorState result) {
        assertNotNull(result);
        assertEquals(SensorState.WARN, result.getState());

        var warningOpt = result.getWarning();
        assertTrue(warningOpt.isPresent());
        assertEquals(2, warningOpt.get().getHigherReads().size());
    }
}