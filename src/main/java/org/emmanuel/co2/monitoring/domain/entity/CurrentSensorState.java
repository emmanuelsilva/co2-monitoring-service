package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Value;

import java.util.Optional;

@Value
public class CurrentSensorState {

    private final Sensor sensor;
    private final SensorState state;
    private final SensorWarning warning;

    public CurrentSensorState(Sensor sensor, SensorState state) {
        this.sensor = sensor;
        this.state = state;
        this.warning = null;
    }

    public CurrentSensorState(Sensor sensor, SensorState state, SensorWarning warning) {
        this.sensor = sensor;
        this.state = state;
        this.warning = warning;
    }

    public Optional<SensorWarning> getWarning() {
        return Optional.ofNullable(this.warning);
    }
}
