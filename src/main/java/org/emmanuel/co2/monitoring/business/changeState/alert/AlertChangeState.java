package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.*;

public class AlertChangeState implements ChangeState {

    private final static int ALERT_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.WARN)
                .measurementIsAbove(ALERT_THRESHOLD)
                .withWarningAttempts(2)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);
        var finishedWarning = SensorWarning.end(warning, measurement.getTimestamp());

        var alert = SensorAlert.from(warning);
        alert.addHigherRead(measurement);

        return new CurrentSensorState(sensor, SensorState.ALERT, finishedWarning, alert);
    }
}
