package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

import java.util.Optional;

public class ComputeCurrentSensorState {

    public CurrentSensorState compute(Sensor sensor, SensorWarning warning) {
        return this.compute(sensor, warning, null);
    }

    public CurrentSensorState compute(Sensor sensor, SensorWarning warning, SensorAlert alert) {
        var warningState = Optional.ofNullable(warning)
                .filter(SensorWarning::isOpened)
                .map(w -> SensorState.WARN)
                .orElse(SensorState.OK);

        var currentState = Optional.ofNullable(alert)
                .filter(SensorAlert::isOpened)
                .map(a -> SensorState.ALERT)
                .orElse(warningState);

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(currentState)
                .warning(warning)
                .alert(alert)
                .build();
    }

}
