package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertDetectedSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private AlertDetectedSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new AlertDetectedSensorStateRule();
    }

    @Test
    void shouldAcceptWhenCurrentStateReachMaxWarnAttempts() {
        var sensor = givenSensor();
        var warnState = givenWarningSateWithMaxAttempts(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenMeasurementIsLowerThanThreshold() {
        var sensor = givenSensor();
        var okState = givenOKState(sensor);
        var lowerThresholdMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = rule.accept(okState, lowerThresholdMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldGenerateAlertStateWhenReachMaxWarnAttempts() {
        var sensor = givenSensor();
        var warnState = givenWarningSateWithMaxAttempts(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var result = rule.defineState(warnState, warningMeasurement);
        assertThatAlertWasCreated(result);
        assertThatWarnWasFinalized(result);
    }

    private void assertThatWarnWasFinalized(CurrentSensorState result) {
        var warningOpt = result.getWarning();
        assertTrue(warningOpt.isPresent());
        assertNotNull(warningOpt.get().getEndAt());
    }

    private void assertThatAlertWasCreated(CurrentSensorState result) {
        assertNotNull(result);
        assertEquals(SensorState.ALERT, result.getState());

        var alertOpt = result.getAlert();
        var warningOpt = result.getWarning();

        assertTrue(alertOpt.isPresent());
        assertTrue(warningOpt.isPresent());

        var alert = alertOpt.get();
        var warning = warningOpt.get();

        var warningReads = warning.getHigherReads().size();
        var expectedReads = warningReads + 1; //all warning reads + 1 alert read

        assertEquals(expectedReads, alert.getHigherReads().size());
        assertNull(alert.getEndAt());
    }


}