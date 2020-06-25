package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

public class NoChangeState implements ChangeState {

    @Override
    public ChangeStateDetectorRule rule() {
        return ChangeStateDetectorRule.start();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        return currentSensorState;
    }
}
