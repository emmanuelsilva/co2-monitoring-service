package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

class ChangeSensorStateTest extends BaseChangeSensorStateTest {

    @Test
    void shouldChangeToWarningState() {
        var sensor = givenSensor();
        var okState = super.givenOKState(sensor);
        var exceededMeasurement =  super.createExceededMeasurement(sensor);

        var changeSensorState = new ChangeSensorState(okState, exceededMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedToWarn(newState);
    }

    @Test
    void shouldChangeToOKStateWhenSensorIsWarnState() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var lowerThresholdMeasurement =super.createLowerThanThresholdMeasurement(sensor);

        var changeSensorState = new ChangeSensorState(warningState, lowerThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedFromWarnToOK(newState);
    }

    @Test
    void shouldIncrementWarningCounter() {
        var sensor = givenSensor();
        var warningState = super.givenWarnState(sensor);
        var exceededMeasurement =  super.createExceededMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(warningState, exceededMeasurement);

        var newState = changeSensorState.change();

        var expectedIncrementedHighReads = warningState.getWarning().get().getHigherReads().size() + 1;
        assertThatWarningCounterWasIncremented(newState, expectedIncrementedHighReads);
    }

    @Test
    void shouldChangeToAlertStateAfterThreeExceedMeasurements() {
        var sensor = givenSensor();
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        warning.addHigherRead(createExceededMeasurement(sensor));
        warning.addHigherRead(createExceededMeasurement(sensor));

        var thirdExceededMeasurement =  super.createExceededMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(buildWarningState(sensor, warning), thirdExceededMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedToAlert(thirdExceededMeasurement, newState);
    }

    @Test
    void shouldIncrementLowerReadWhenSensorIsInAlertState() {
        var sensor = givenSensor();
        var alertState = givenAlertState(sensor);
        var lowerThresholdMeasurement = super.createLowerThanThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(alertState, lowerThresholdMeasurement);

        var newState = changeSensorState.change();
        assertThatAlertLowerCounterWasIncremented(newState);
    }

    @Test
    void shouldChangeFromAlertToOKStateAfterThreeLowerThanThresholdMeasurement() {
        var sensor = givenSensor();

        var alert = SensorAlert.create(sensor, now());
        alert.addLowerRead(createLowerThanThresholdMeasurement(sensor));
        alert.addLowerRead(createLowerThanThresholdMeasurement(sensor));

        var thirdLowerMeasurement = super.createLowerThanThresholdMeasurement(sensor);
        var changeSensorState = new ChangeSensorState(buildAlertState(alert), thirdLowerMeasurement);

        var newState = changeSensorState.change();
        assertThatSensorStatusWasChangedFromAlertToOK(newState);
    }
}