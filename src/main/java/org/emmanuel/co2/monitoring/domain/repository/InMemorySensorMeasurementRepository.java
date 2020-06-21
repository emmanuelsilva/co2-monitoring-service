package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemorySensorMeasurementRepository implements SensorMeasurementRepository {

    private Set<SensorMeasurement> sensorMeasurements;

    @PostConstruct
    public void postConstruct() {
        this.sensorMeasurements = new HashSet<>();
    }

    @Override
    public SensorMeasurement save(SensorMeasurement sensorMeasurement) {
        this.sensorMeasurements.add(sensorMeasurement);
        return sensorMeasurement;
    }

    @Override
    public List<SensorMeasurement> findAllMeasurementBySensorIdAndTimestampAfter(String sensorId, OffsetDateTime startPeriod) {
        return sensorMeasurements.stream()
                .filter(m -> m.getSensor().getId().equals(sensorId) && m.getTimestamp().isAfter(startPeriod))
                .collect(Collectors.toList());
    }
}
