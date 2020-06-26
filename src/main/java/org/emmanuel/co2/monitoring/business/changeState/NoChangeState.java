package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

/**
 * Used to represent a null object, when no ChangeState was detected.
 * This implementation will return the current sensor state.
 **/
class NoChangeState implements ChangeState {

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder().build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        return currentSensorState;
    }
}
