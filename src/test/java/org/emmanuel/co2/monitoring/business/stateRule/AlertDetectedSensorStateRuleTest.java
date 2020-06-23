package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertDetectedSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private AlertDetectedSensorStateRule rule;

    @Override
    SensorStateRule getRule() {
        return rule;
    }

    @Override
    SensorState getState() {
        return SensorState.ALERT;
    }

    @BeforeEach
    void setUp() {
        this.rule = new AlertDetectedSensorStateRule();
    }

    @Test
    void shouldAcceptWhenStateIsWarningAndReachMaxWarnAttempts() {
        var sensor = new Sensor("123");
        var warnState = givenWarningWithReachMaxWarnAttempt(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldChangeWarnToAlertWhenReachMaxWarnAttempts() {
        var sensor = new Sensor("123");
        var warnState = givenWarningWithReachMaxWarnAttempt(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var result = rule.defineState(warnState, warningMeasurement);
        assertThatAlertWasCreated(result);
        assertThatWarnWasEnded(result);
    }

    private void assertThatWarnWasEnded(CurrentSensorState result) {
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

        var warningReads = warningOpt.get().getHigherReads().size();
        var expectedReads = warningReads + 1; //all warning reads + 1 alert read

        assertEquals(expectedReads, alertOpt.get().getHigherReads().size());
    }


}