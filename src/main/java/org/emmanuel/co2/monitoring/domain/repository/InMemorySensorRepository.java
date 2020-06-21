package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemorySensorRepository implements SensorRepository {

    private Set<Sensor> sensors;

    @PostConstruct
    public void postConstruct() {
        this.sensors = new HashSet<>();
    }

    @Override
    public Sensor save(Sensor sensor) {
        this.sensors.add(sensor);
        return sensor;
    }

    @Override
    public Optional<Sensor> findById(String id) {
        return sensors.stream().filter(s -> s.getId().equals(id)).findFirst();
    }
}
