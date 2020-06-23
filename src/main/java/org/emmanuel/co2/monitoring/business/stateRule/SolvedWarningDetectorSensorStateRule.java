package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.springframework.stereotype.Component;

@Component
public class SolvedWarningDetectorSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        return SensorState.WARN.equals(currentState.getState()) && measurement.getValue() < THRESHOLD;
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);
        var endedWarning = SensorWarning.end(warning, measurement.getTimestamp());

        return new CurrentSensorState(sensor, SensorState.OK, endedWarning);
    }
}
