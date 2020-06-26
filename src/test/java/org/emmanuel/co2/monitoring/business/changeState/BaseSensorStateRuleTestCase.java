package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class BaseSensorStateRuleTestCase {

    protected Sensor givenSensor() {
        var sensor = new Sensor("123");
        return sensor;
    }

    protected CurrentSensorState givenOKState(Sensor sensor) {
        return new CurrentSensorState(sensor, SensorState.OK);
    }

    protected OffsetDateTime now() {
        return OffsetDateTime.now();
    }

    protected CurrentSensorState givenWarningSateWithMaxAttempts(Sensor sensor) {
        SensorWarning warning = buildSensorWarningWithMaxAttempts(sensor);
        return new CurrentSensorState(sensor, SensorState.WARN, warning);
    }

    private SensorWarning buildSensorWarningWithMaxAttempts(Sensor sensor) {
        var warning = SensorWarning.create(sensor, now());

        IntStream.range(1, SensorThresholdConfiguration.MAX_ATTEMPTS.value())
                .forEach(i -> warning.addHigherRead(getHigherThresholdMeasurement(sensor)));

        return warning;
    }

    protected CurrentSensorState givenWarnState(Sensor sensor) {
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        warning.addHigherRead(getHigherThresholdMeasurement(sensor));

        return new CurrentSensorState(sensor, SensorState.WARN, warning);
    }

    protected CurrentSensorState givenAlertState(Sensor sensor) {
        var warning = buildSensorWarningWithMaxAttempts(sensor);
        var alert = SensorAlert.from(warning);

        return new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
    }

    protected CurrentSensorState givenAlertStateWithMaxLowerAttempts(Sensor sensor) {
        var alert = SensorAlert.create(sensor, now());

        IntStream.range(1, SensorThresholdConfiguration.MAX_ATTEMPTS.value())
                .forEach(i -> alert.addLowerRead(getLowerThresholdMeasurement(sensor)));

       return new CurrentSensorState(sensor, SensorState.ALERT, alert);
    }

    protected SensorMeasurement getHigherThresholdMeasurement(Sensor sensor) {
        return new SensorMeasurement(
                sensor,
                SensorThresholdConfiguration.THRESHOLD.value() + 500,
                OffsetDateTime.now());
    }

    protected SensorMeasurement getLowerThresholdMeasurement(Sensor sensor) {
        return new SensorMeasurement(
                sensor,
                SensorThresholdConfiguration.THRESHOLD.value() - 100,
                OffsetDateTime.now());
    }

    protected void assertThatSensorStatusWasChangedToWarn(CurrentSensorState newState) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());
        assertTrue(newState.getWarning().isPresent());
        assertEquals(1, newState.getWarning().get().getHigherReads().size());
    }

    protected void assertThatWarningCounterWasIncremented(CurrentSensorState newState, int expectedIncrementedHighReads) {
        assertNotNull(newState);
        assertEquals(SensorState.WARN, newState.getState());
        assertTrue(newState.getWarning().isPresent());
        assertEquals(expectedIncrementedHighReads, newState.getWarning().get().getHigherReads().size());
    }

    protected void assertThatSensorStatusWasChangedFromWarnToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getWarning().isPresent());

        var warning = newState.getWarning().get();
        assertNotNull(warning.getEndAt());
    }

    protected void assertThatSensorStatusWasChangedToAlert(SensorMeasurement higherThresholdMeasurement, CurrentSensorState newState) {
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

    protected void assertThatAlertLowerCounterWasIncremented(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getAlert().isPresent());

        var alert = newState.getAlert().get();
        assertEquals(1, alert.getLowerReads().size());
        assertNull(alert.getEndAt());
    }

    protected void assertThatSensorStatusWasChangedFromAlertToOK(CurrentSensorState newState) {
        assertNotNull(newState);
        assertTrue(newState.getAlert().isPresent());

        var alert = newState.getAlert().get();
        assertNotNull(alert.getEndAt());
    }
}
