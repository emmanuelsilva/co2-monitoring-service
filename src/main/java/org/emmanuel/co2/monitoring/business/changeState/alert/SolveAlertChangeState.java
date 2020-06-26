package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

public class SolveAlertChangeState implements ChangeState {

    private final static int ALERT_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.ALERT)
                .measurementIsBelow(ALERT_THRESHOLD)
                .withAlertLowerAttempts(2)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var alert = currentState.getAlert().orElseThrow(IllegalStateException::new);
        var resolvedAlert = SensorAlert.end(alert, measurement.getTimestamp());

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.OK)
                .alert(resolvedAlert)
                .build();

    }
}
