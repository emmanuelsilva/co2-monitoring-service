package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;

import java.util.Optional;

public interface SensorRepository {

    Sensor save(Sensor sensor);

    Optional<Sensor> findById(String id);
}
