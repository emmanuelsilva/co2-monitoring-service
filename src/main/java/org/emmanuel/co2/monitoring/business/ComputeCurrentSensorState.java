package org.emmanuel.co2.monitoring.business;

import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;

import java.util.Optional;

public class ComputeCurrentSensorState {

    public CurrentSensorState compute(Sensor sensor, SensorWarning warning) {
        var state = Optional.ofNullable(warning)
                .filter(w -> w.getEndAt() == null)
                .map(w -> SensorState.WARN)
                .orElse(SensorState.OK);

        return new CurrentSensorState(sensor, state, warning);
    }

}
