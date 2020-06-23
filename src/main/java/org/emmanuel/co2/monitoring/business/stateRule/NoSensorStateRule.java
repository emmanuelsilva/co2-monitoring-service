package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

public class NoSensorStateRule implements SensorStateRule {

    @Override
    public boolean accept(CurrentSensorState currentState, SensorMeasurement measurement) {
        return false;
    }

    @Override
    public CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement) {
        return currentState;
    }
}
