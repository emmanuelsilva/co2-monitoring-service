package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

public interface SensorMeasurementRepository {

    SensorMeasurement save(SensorMeasurement sensorMeasurement);
}
