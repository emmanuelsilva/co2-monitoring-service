package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.springframework.stereotype.Component;

@Component
public class AlertDetectedSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();
    private static final int MAX_WARNING_ATTEMPTS = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        return willReachMaxWarningAttempts(currentState, measurement);
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);

        var endedWarning = SensorWarning.end(warning, measurement.getTimestamp());

        var alert = SensorAlert.from(warning);
        alert.addHigherRead(measurement);

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, endedWarning, alert);

        return alertState;
    }

    private boolean willReachMaxWarningAttempts(CurrentSensorState currentState, SensorMeasurement measurement) {
        var maxWarningAttemptsReach = currentState.getWarning()
                .map(w -> w.getHigherReads().size() + 1 == MAX_WARNING_ATTEMPTS)
                .orElse(false);

        return SensorState.WARN.equals(currentState.getState())
                && measurement.getValue() > THRESHOLD
                && maxWarningAttemptsReach;
    }
}
