package org.emmanuel.co2.monitoring.domain.repository.inMemory;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class InMemorySensorRepository extends InMemoryRepository<Sensor> implements SensorRepository {

    @Override
    public Sensor save(Sensor sensor) {
        return super.save(sensor);
    }

    @Override
    public Optional<Sensor> findById(String id) {
        return super.find(s -> s.getId().equals(id));
    }
}
