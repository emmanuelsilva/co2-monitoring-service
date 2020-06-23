package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.springframework.stereotype.Component;

@Component
public class SolvedAlertSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();
    private static final int MAX_ATTEMPTS = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        var maxLowerReadsAttempt = currentState.getAlert()
                .map(a -> a.getLowerReads().size() + 1 == MAX_ATTEMPTS)
                .orElse(false);

        return SensorState.ALERT.equals(currentState.getState())
                && measurement.getValue() < THRESHOLD
                && maxLowerReadsAttempt;
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var alert = currentState.getAlert().orElseThrow(IllegalStateException::new);
        var resolvedAlert = SensorAlert.end(alert, measurement.getTimestamp());

        return new CurrentSensorState(sensor, SensorState.OK, resolvedAlert);
    }
}
