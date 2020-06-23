package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

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
