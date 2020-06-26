package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

/**
 * Strategy pattern used to change a sensor to a new state based on their current status.
 **/
public interface ChangeState {

    ChangeStateDetectorRule rule();

    CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement);

}
