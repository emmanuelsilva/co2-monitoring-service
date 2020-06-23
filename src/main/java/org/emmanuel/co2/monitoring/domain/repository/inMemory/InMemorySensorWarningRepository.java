package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InMemorySensorWarningRepository extends InMemoryRepository<SensorWarning> implements SensorWarningRepository {

    @Override
    public SensorWarning save(SensorWarning warning) {
        return super.save(warning);
    }

    @Override
    public Optional<SensorWarning> findActiveBySensorId(String sensorId) {
        return super.find(w -> w.getSensor().getId().equals(sensorId) && w.isOpened());
    }
}
