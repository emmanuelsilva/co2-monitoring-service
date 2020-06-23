package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;

import java.util.List;

public interface SensorAlertService {

    List<SensorAlert> getAlerts(String sensorId);

}
