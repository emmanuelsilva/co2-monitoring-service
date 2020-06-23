package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;

import java.util.Optional;

public interface SensorAlertRepository {

    SensorAlert save(SensorAlert alert);

    Optional<SensorAlert> findActiveBySensorId(String sensorId);

}
