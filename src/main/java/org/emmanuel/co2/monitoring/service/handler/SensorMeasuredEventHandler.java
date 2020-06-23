package org.emmanuel.co2.monitoring.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.business.ComputeCurrentSensorState;
import org.emmanuel.co2.monitoring.business.stateRule.NoSensorStateRule;
import org.emmanuel.co2.monitoring.business.stateRule.SensorStateRule;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorMeasuredEventHandler {

    private final SensorWarningRepository sensorWarningRepository;
    private final List<SensorStateRule> sensorStateRules;

    @Async
    @EventListener
    public void handleMeasuredEvent(SensorMeasuredEvent event) {
        var measurement = event.getMeasurement();
        var sensor = measurement.getSensor();
        CurrentSensorState currentState = getCurrentSensorState(sensor);

        log.info("current sensor state {}", currentState);
        log.info("processing new measured event {}", measurement);

        var sensorRule = getSensorRule(currentState, measurement);
        var newState = sensorRule.defineState(currentState, measurement);

        log.info("new state {}", newState);

        newState.getWarning().ifPresent(newWarning -> {
            var oldWarning = currentState.getWarning().orElse(null);

            if (!newWarning.equals(oldWarning)) {
                log.info("warning state changed, saving on database");
                this.sensorWarningRepository.save(newWarning);
            }
        });

    }

    private CurrentSensorState getCurrentSensorState(Sensor sensor) {
        var warning = sensorWarningRepository.findActiveBySensorId(sensor.getId()).orElse(null);

        ComputeCurrentSensorState sensorState = new ComputeCurrentSensorState();
        return sensorState.compute(sensor, warning);
    }

    private SensorStateRule getSensorRule(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        var sensorStateRule = sensorStateRules.stream()
                .filter(rule -> rule.accept(currentSensorState, measurement))
                .findFirst()
                .orElse(new NoSensorStateRule());

        log.info("{} has defined to process the measurement {}", sensorStateRule.getClass().getSimpleName(), measurement);

        return sensorStateRule;
    }
}
