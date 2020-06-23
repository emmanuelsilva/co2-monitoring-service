package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;

@Component
public class InMemorySensorMeasurementRepository extends InMemoryRepository<SensorMeasurement> implements SensorMeasurementRepository {

    @Override
    public SensorMeasurement save(SensorMeasurement sensorMeasurement) {
        return super.save(sensorMeasurement);
    }

    @Override
    public List<SensorMeasurement> findAllMeasurementBySensorIdAndTimestampAfter(String sensorId, OffsetDateTime startPeriod) {
        Predicate<SensorMeasurement> filter = (m) -> m.getSensor().getId().equals(sensorId)
                && m.getTimestamp().isAfter(startPeriod);

        return super.findAll(filter);
    }
}
