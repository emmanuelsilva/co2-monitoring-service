package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.springframework.stereotype.Component;

@Component
public class IncrementWarningReadingSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();
    private static final int MAX_WARNING_ATTEMPTS = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        return willStayWarnState(currentState, measurement);
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);

        var incrementedWarning = SensorWarning.copy(warning);
        incrementedWarning.addHigherRead(measurement);

        var warningState = new CurrentSensorState(sensor, SensorState.WARN, incrementedWarning);

        return warningState;
    }

    private boolean willStayWarnState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var maxWarningAttempts = currentState.getWarning()
                .map(w -> w.getHigherReads().size() < MAX_WARNING_ATTEMPTS - 1)
                .orElse(false);

        return SensorState.WARN.equals(currentState.getState())
                && measurement.getValue() > THRESHOLD
                && maxWarningAttempts;
    }
}
