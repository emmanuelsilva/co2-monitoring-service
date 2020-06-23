package org.emmanuel.co2.monitoring.domain.repository;

import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class InMemorySensorAlertRepository implements SensorAlertRepository {

    private Set<SensorAlert> alerts;

    @PostConstruct
    public void postConstruct() {
        this.alerts = new HashSet<>();
    }

    @Override
    public SensorAlert save(SensorAlert alert) {
        this.alerts.add(alert);
        return alert;
    }

    @Override
    public Optional<SensorAlert> findActiveBySensorId(String sensorId) {
        return this.alerts.stream()
                .filter(a -> a.getSensor().getId().equals(sensorId) && a.getEndAt() == null)
                .findFirst();
    }
}
