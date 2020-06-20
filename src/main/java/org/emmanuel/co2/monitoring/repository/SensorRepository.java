package org.emmanuel.co2.monitoring.repository;

import org.emmanuel.co2.monitoring.domain.Sensor;

import java.util.Optional;

public interface SensorRepository {

    Sensor save(Sensor sensor);

    Optional<Sensor> findById(String id);
}
