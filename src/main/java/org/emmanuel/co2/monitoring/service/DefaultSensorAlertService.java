package org.emmanuel.co2.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultSensorAlertService implements SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Override
    public List<SensorAlert> getAlerts(String sensorId) {
        return this.sensorAlertRepository.findAllBySensorId(sensorId);
    }
}
