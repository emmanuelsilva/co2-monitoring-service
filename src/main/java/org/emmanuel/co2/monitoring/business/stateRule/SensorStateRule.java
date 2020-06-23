package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

public interface SensorStateRule {

    boolean accept(CurrentSensorState currentState, SensorMeasurement measurement);

    CurrentSensorState defineState(CurrentSensorState currentState, SensorMeasurement measurement);

}
