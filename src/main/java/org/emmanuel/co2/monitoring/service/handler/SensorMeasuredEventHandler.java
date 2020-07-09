package org.emmanuel.co2.monitoring.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.business.ComputeCurrentSensorState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorMeasuredEventHandler {

    private final SensorWarningRepository sensorWarningRepository;
    private final SensorAlertRepository sensorAlertRepository;

    @Async
    @EventListener
    public void handleMeasuredEvent(SensorMeasuredEvent event) {
        var measurement = event.getMeasurement();
        var sensor = measurement.getSensor();
        var currentState = getCurrentSensorState(sensor);

        var changeSensorState = new ChangeSensorState();
        var nextState = changeSensorState.change(currentState, measurement);

        log.info("current sensor state {}", currentState);
        log.info("processing new measured event {}", measurement);
        log.info("next state {}", nextState);

        nextState.getWarning().ifPresent(sensorWarningRepository::save);
        nextState.getAlert().ifPresent(sensorAlertRepository::save);
    }

    private CurrentSensorState getCurrentSensorState(Sensor sensor) {
        var warning = sensorWarningRepository.findActiveBySensorId(sensor.getId()).orElse(null);
        var alert = sensorAlertRepository.findActiveBySensorId(sensor.getId()).orElse(null);

        var sensorState = new ComputeCurrentSensorState();
        return sensorState.compute(sensor, warning, alert);
    }
}
