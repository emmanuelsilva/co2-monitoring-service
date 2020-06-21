package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;

public interface SensorMeasurementService {

    void measure(SensorMeasurementRequest request);

}
