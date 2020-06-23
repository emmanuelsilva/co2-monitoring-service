package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.springframework.stereotype.Component;

@Component
public class WarningDetectedSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        return warnStateDetected(currentState, measurement);
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var sensorWarning = SensorWarning.create(sensor, measurement.getTimestamp());
        sensorWarning.addWarningRead(measurement);

        var warningState = new CurrentSensorState(sensor, SensorState.WARN, sensorWarning);

        return warningState;
    }

    private boolean warnStateDetected(CurrentSensorState currentState, SensorMeasurement measurement) {
        return SensorState.OK.equals(currentState.getState()) && measurement.getValue() > THRESHOLD;
    }
}
