package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.*;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

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
}
