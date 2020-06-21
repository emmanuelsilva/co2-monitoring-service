package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SensorMetricCalculatorTest {

    @Test
    void shouldComputeMetricsForFilledMeasurements() {
        var expectedMax = 10;
        var expectedAvg = 5.5;

        var sensor = new Sensor("123");
        var now = OffsetDateTime.now();

        var measurements = IntStream.rangeClosed(1, 10)
          .mapToObj(i -> new SensorMeasurement(sensor, i, now))
          .collect(Collectors.toList());

        SensorMetricCalculator metricCalculator = new SensorMetricCalculator();
        var metrics = metricCalculator.computeMetrics(measurements);

        assertNotNull(metrics);
        assertEquals(expectedMax, metrics.getMax());
        assertEquals(expectedAvg, metrics.getAverage());
    }

    @Test
    void shouldComputeAsZeroForEmptyMeasurements() {
        SensorMetricCalculator metricCalculator = new SensorMetricCalculator();
        var metrics = metricCalculator.computeMetrics(Collections.emptyList());

        assertNotNull(metrics);
        assertEquals(0, metrics.getMax());
        assertEquals(0, metrics.getAverage());
    }
}