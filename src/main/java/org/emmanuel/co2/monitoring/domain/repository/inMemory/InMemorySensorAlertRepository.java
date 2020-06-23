package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InMemorySensorAlertRepository extends InMemoryRepository<SensorAlert> implements SensorAlertRepository {

    @Override
    public SensorAlert save(SensorAlert alert) {
        return super.save(alert);
    }

    @Override
    public Optional<SensorAlert> findActiveBySensorId(String sensorId) {
        return super.find(a -> a.getSensor().getId().equals(sensorId) && a.isOpened());
    }
}
