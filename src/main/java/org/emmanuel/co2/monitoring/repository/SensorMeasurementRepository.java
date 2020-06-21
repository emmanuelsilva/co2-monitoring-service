package org.emmanuel.co2.monitoring.repository;

import org.emmanuel.co2.monitoring.domain.SensorMeasurement;

import java.util.Optional;

public interface SensorMeasurementRepository {

    Optional<SensorMeasurement> save(SensorMeasurement sensorMeasurement);
}
