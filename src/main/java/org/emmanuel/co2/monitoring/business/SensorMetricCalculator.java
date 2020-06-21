package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;

import java.util.List;

public class SensorMetricCalculator {

    public SensorMetric computeMetrics(List<SensorMeasurement> measurements) {
        var statistics = measurements.stream().mapToInt(SensorMeasurement::getValue).summaryStatistics();
        return new SensorMetric(statistics.getMax(), statistics.getAverage());
    }

}
