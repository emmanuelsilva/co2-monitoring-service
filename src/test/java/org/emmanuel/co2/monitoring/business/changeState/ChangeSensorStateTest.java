package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    private void assertThatSensorStatusWasChangedToWarn(CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());
        assertTrue(newState.getWarning().isPresent());
        assertEquals(1, newState.getWarning().get().getHigherReads().size());
    }

    private void assertThatSensorStatusWasChangedFromWarnToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getWarning().isPresent());

        var warning = newState.getWarning().get();
        assertNotNull(warning.getEndAt());
    }

    private void assertThatWarningCounterWasIncremented(CurrentSensorState newState, int expectedIncrementedHighReads) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());
        assertTrue(newState.getWarning().isPresent());
        assertEquals(expectedIncrementedHighReads, newState.getWarning().get().getHigherReads().size());
    }

    private void assertThatSensorStatusWasChangedToAlert(SensorMeasurement higherThresholdMeasurement, CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.ALERT, newState.getState());

        assertTrue(newState.getWarning().isPresent());
        assertNotNull(newState.getWarning().get().getEndAt());
        assertTrue(newState.getAlert().isPresent());
        assertNull(newState.getAlert().get().getEndAt());

        var warning = newState.getWarning().get();
        var alert = newState.getAlert().get();

        var expectedReads = new ArrayList<>(warning.getHigherReads());
        expectedReads.add(higherThresholdMeasurement.getValue());

        assertEquals(expectedReads, alert.getHigherReads());
    }

    private void assertThatAlertLowerCounterWasIncremented(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getAlert().isPresent());

        var alert = newState.getAlert().get();
        assertEquals(1, alert.getLowerReads().size());
        assertNull(alert.getEndAt());
    }

    private void assertThatSensorStatusWasChangedFromAlertToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getAlert().isPresent());

        var alert = newState.getAlert().get();
        assertNotNull(alert.getEndAt());
    }
}