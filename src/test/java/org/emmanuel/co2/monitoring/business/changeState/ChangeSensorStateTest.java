package org.emmanuel.co2.monitoring.business.changeState;

import org.junit.jupiter.api.Test;

class ChangeSensorStateTest extends BaseSensorStateRuleTestCase {

    @Test
    void shouldChangeToWarningState() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);

        var changeSensorState = new ChangeSensorState(okState, higherThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedToWarn(newState);
    }

    @Test
    void shouldChangeToOKStateWhenSensorIsWarnState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);

        var changeSensorState = new ChangeSensorState(warningState, lowerThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedFromWarnToOK(newState);
    }

    @Test
    void shouldIncrementWarningCounter() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var higherThresholdMeasurement =  super.getHigherThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(warningState, higherThresholdMeasurement);

        var newState = changeSensorState.change();

        var expectedIncrementedHighReads = warningState.getWarning().get().getHigherReads().size() + 1;
        assertThatWarningCounterWasIncremented(newState, expectedIncrementedHighReads);
    }

    @Test
    void shouldChangeToAlertState() {
        var sensor = givenSensor();
        var warningState = super.givenWarningSateWithMaxAttempts(sensor);
        var higherThresholdMeasurement = super.getHigherThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(warningState, higherThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedToAlert(higherThresholdMeasurement, newState);
    }

    @Test
    void shouldIncrementLowerReadWhenSensorIsInAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerThresholdMeasurement =super.getLowerThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(alertState, lowerThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatAlertLowerCounterWasIncremented(newState);
    }

    @Test
    void shouldChangeToOKStateWhenSensorIsAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertStateWithMaxLowerAttempts(sensor);
        var lowerThresholdMeasurement = super.getLowerThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(alertState, lowerThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedFromAlertToOK(newState);
    }
}