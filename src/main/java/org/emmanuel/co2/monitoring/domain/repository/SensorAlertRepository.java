package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;

import java.util.List;

public interface SensorAlertRepository extends ActiveBySensorFinder<SensorAlert> {

    SensorAlert save(SensorAlert alert);

    List<SensorAlert> findAllBySensorId(String sensorId);

}
