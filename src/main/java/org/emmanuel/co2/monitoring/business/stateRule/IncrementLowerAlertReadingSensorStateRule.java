package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.springframework.stereotype.Component;

@Component
public class IncrementLowerAlertReadingSensorStateRule implements SensorStateRule {

    private static final int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();
    private static final int MAX_ATTEMPTS = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        var alert = currentState.getAlert().orElseThrow(IllegalStateException::new);

        return SensorState.ALERT.equals(currentState.getState())
                && measurement.getValue() < THRESHOLD
                && alert.getLowerReads().size() < MAX_ATTEMPTS;
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);
        var incrementedLowerReadAlert = currentState.getAlert().orElseThrow(IllegalStateException::new);

        incrementedLowerReadAlert.addLowerRead(measurement);

        return new CurrentSensorState(sensor, SensorState.ALERT, warning, incrementedLowerReadAlert);
    }
}
