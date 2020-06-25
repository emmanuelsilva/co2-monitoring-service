package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

public interface ChangeState {

    ChangeStateDetectorRule rule();

    CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement);

}
