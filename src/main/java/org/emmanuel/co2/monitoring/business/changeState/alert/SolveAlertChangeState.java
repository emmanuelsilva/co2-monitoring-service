package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;

public class SolveAlertChangeState implements ChangeState {

    private final static int ALERT_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return ChangeStateDetectorRule
                .start()
                .whenStateIs(SensorState.ALERT)
                .measurementIsBelow(ALERT_THRESHOLD)
                .withAlertLowerAttempts(2);
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var alert = currentState.getAlert().orElseThrow(IllegalStateException::new);
        var resolvedAlert = SensorAlert.end(alert, measurement.getTimestamp());

        return new CurrentSensorState(sensor, SensorState.OK, resolvedAlert);
    }
}
