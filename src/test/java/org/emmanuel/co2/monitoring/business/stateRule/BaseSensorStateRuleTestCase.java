package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertFalse;

public abstract class BaseSensorStateRuleTestCase {

    abstract SensorStateRule getRule();

    abstract SensorState getState();

    @Test
    void shouldNotAcceptWhenMeasurementIsLowerThanThreshold() {
        var sensor = new Sensor("123");
        var okState = new CurrentSensorState(sensor, getState());
        var nonWarningMeasurement = getLowerThresholdMeasurement(sensor);

        var accepted = getRule().accept(okState, nonWarningMeasurement);
        assertFalse(accepted);
    }

    protected CurrentSensorState givenWarningWithReachMaxWarnAttempt(Sensor sensor) {
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);

        IntStream.rangeClosed(1, SensorThresholdConfiguration.MAX_ATTEMPTS.value())
                .forEach(i -> warning.addHigherRead(new SensorMeasurement(sensor, i, now)));

        return new CurrentSensorState(sensor, SensorState.WARN, warning);
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

}
