package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.BaseSensorStateRuleTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WarnChangeStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldMatchWhenSensorIsOK() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        WarnChangeState warnChangeState = new WarnChangeState();
        var match = warnChangeState.rule().match(okState, higherThresholdMeasurement);

        assertTrue(match);
    }

    @Test
    void shouldNotMatch() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var lowerThresholdMeasurement =  super.getLowerThresholdMeasurement(sensor);

        WarnChangeState warnChangeState = new WarnChangeState();
        var match = warnChangeState.rule().match(okState, lowerThresholdMeasurement);

        assertFalse(match);
    }

    @Test
    void shouldChangeToWarnState() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        WarnChangeState warnChangeState = new WarnChangeState();
        var newState = warnChangeState.changeState(okState, higherThresholdMeasurement);

        assertThatSensorStatusWasChangedToWarn(newState);
    }
}