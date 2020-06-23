package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.SensorState;

public interface SensorStatusService {

    SensorState getCurrentState(String sensorId);
}
