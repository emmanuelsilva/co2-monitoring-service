package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

import java.time.OffsetDateTime;
import java.util.List;

public interface SensorMeasurementRepository {

    SensorMeasurement save(SensorMeasurement sensorMeasurement);

    List<SensorMeasurement> findAllMeasurementBySensorIdAndTimestampAfter(String sensorId, OffsetDateTime startPeriod);
}
