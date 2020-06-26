package org.emmanuel.co2.monitoring.business.changeState.alert;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;

public class IncrementAlertLowerReadingChangeState implements ChangeState {

    private final static int ALERT_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.ALERT)
                .measurementIsBelow(ALERT_THRESHOLD)
                .withAlertLowerAttempts(1)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();

        var incrementedLowerReadAlert = currentState.getAlert().orElseThrow(IllegalStateException::new);
        incrementedLowerReadAlert.addLowerRead(measurement);

        return new CurrentSensorState(sensor, SensorState.ALERT, incrementedLowerReadAlert);
    }
}
