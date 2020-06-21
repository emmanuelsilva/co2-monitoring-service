package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;

import java.util.List;

public class SensorMetricCalculator {

    public SensorMetric computeMetrics(List<SensorMeasurement> measurements) {

        if (measurements.isEmpty()) {
            return new SensorMetric(0, 0.0);
        }

        var statistics = measurements.stream().mapToInt(SensorMeasurement::getValue).summaryStatistics();
        return new SensorMetric(statistics.getMax(), getRounded(statistics.getAverage()));
    }

    private double getRounded(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

}
