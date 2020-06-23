package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemorySensorWarningRepository implements SensorWarningRepository {

    private Set<SensorWarning> warnings;

    @PostConstruct
    public void postConstruct() {
        this.warnings = new HashSet<>();
    }

    @Override
    public SensorWarning save(SensorWarning warning) {
        this.warnings.add(warning);
        return warning;
    }

    @Override
    public Optional<SensorWarning> findActiveBySensorId(String sensorId) {
        return this.warnings
                .stream()
                .filter(w -> w.getSensor().getId().equals(sensorId) && w.getEndAt() == null)
                .findFirst();
    }
}
