package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

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
}
