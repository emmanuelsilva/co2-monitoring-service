package org.emmanuel.co2.monitoring.business.changeState;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChangeSensorStateTest extends BaseSensorStateRuleTestCase {

    private ChangeSensorState changeSensorState;

    @BeforeEach
    void setUp() {
        this.changeSensorState = new ChangeSensorState();
    }

    @Test
    void shouldChangeToWarningState() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        var newState = this.changeSensorState.change(okState, higherThresholdMeasurement);
        assertThatSensorStatusWasChangedToWarn(newState);
    }

    @Test
    void shouldChangeToOKStateWhenSensorIsWarnState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var newState = this.changeSensorState.change(warningState, lowerThresholdMeasurement);
        assertThatSensorStatusWasChangedFromWarnToOK(newState);
    }

    @Test
    void shouldIncrementWarningCounter() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);
        var newState = this.changeSensorState.change(warningState, higherThresholdMeasurement);

        var expectedIncrementedHighReads = warningState.getWarning().get().getHigherReads().size() + 1;
        assertThatWarningCounterWasIncremented(newState, expectedIncrementedHighReads);
    }

    @Test
    void shouldChangeToAlertState() {
        var sensor = givenSensor();
        var warningState = super.givenWarningSateWithMaxAttempts(sensor);
        var higherThresholdMeasurement = super.getHigherThresholdMeasurement(sensor);

        var newState = this.changeSensorState.change(warningState, higherThresholdMeasurement);
        assertThatSensorStatusWasChangedToAlert(higherThresholdMeasurement, newState);
    }

    @Test
    void shouldIncrementLowerReadWhenSensorIsInAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var newState = this.changeSensorState.change(alertState, lowerThresholdMeasurement);
        assertThatAlertLowerCounterWasIncremented(newState);
    }

    @Test
    void shouldChangeToOKStateWhenSensorIsAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var newState = this.changeSensorState.change(alertState, lowerThresholdMeasurement);
        assertThatSensorStatusWasChangedFromAlertToOK(newState);
    }
}