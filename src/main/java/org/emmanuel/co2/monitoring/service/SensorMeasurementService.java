package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;

public interface SensorMeasurementService {

    SensorMeasurement measure(SensorMeasurementRequest request);

}
