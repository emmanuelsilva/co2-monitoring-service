package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;

import java.util.Optional;

public interface SensorWarningRepository {

    SensorWarning save(SensorWarning warning);

    Optional<SensorWarning> findActiveBySensorId(String sensorId);

}
