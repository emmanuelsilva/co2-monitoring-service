package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;

public interface SensorMetricService {

    SensorMetric getLast30DaysMetrics(String sensorId);

}
