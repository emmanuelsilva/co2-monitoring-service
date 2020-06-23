package org.emmanuel.co2.monitoring.service;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.business.ComputeCurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.excpetion.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultSensorStatusService implements SensorStatusService {

    private final SensorRepository sensorRepository;
    private final SensorWarningRepository sensorWarningRepository;
    private final SensorAlertRepository sensorAlertRepository;

    @Override
    public SensorState getCurrentState(String sensorId) {
        var sensor = this.sensorRepository
                .findById(sensorId)
                .orElseThrow(ResourceNotFoundException::new);

        var warning = sensorWarningRepository
                .findActiveBySensorId(sensorId)
                .orElse(null);

        var alert = sensorAlertRepository
                .findActiveBySensorId(sensorId)
                .orElse(null);

        ComputeCurrentSensorState computeCurrentSensorState = new ComputeCurrentSensorState();
        var state = computeCurrentSensorState.compute(sensor, warning, alert);

        return state.getState();
    }
}
