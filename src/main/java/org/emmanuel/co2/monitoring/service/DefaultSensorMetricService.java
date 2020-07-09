package org.emmanuel.co2.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.business.SensorMetricCalculator;
import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class DefaultSensorMetricService implements SensorMetricService {

    private final SensorMeasurementRepository repository;

    @Override
    public SensorMetric getLast30DaysMetrics(String sensorId) {
        var now = OffsetDateTime.now().withNano(0);
        var last30DaysPeriod = now.minus(30, ChronoUnit.DAYS);

        var measurements = this.repository
                .findAllMeasurementBySensorIdAndTimestampAfter(sensorId, last30DaysPeriod);

        var sensorMetricCalculator = new SensorMetricCalculator();
        return sensorMetricCalculator.computeMetrics(measurements);
    }
}
