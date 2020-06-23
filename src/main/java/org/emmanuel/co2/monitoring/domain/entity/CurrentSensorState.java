package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Value;

import java.util.Optional;

@Value
public class CurrentSensorState {

    private final Sensor sensor;
    private final SensorState state;
    private final SensorWarning warning;
    private final SensorAlert alert;

    public CurrentSensorState(Sensor sensor, SensorState state) {
        this.sensor = sensor;
        this.state = state;
        this.warning = null;
        this.alert = null;
    }

    public CurrentSensorState(Sensor sensor, SensorState state, SensorWarning warning) {
        this.sensor = sensor;
        this.state = state;
        this.warning = warning;
        this.alert = null;
    }

    public CurrentSensorState(Sensor sensor, SensorState state, SensorWarning warning, SensorAlert alert) {
        this.sensor = sensor;
        this.state = state;
        this.warning = warning;
        this.alert = alert;
    }

    public Optional<SensorWarning> getWarning() {
        return Optional.ofNullable(this.warning);
    }

    public Optional<SensorAlert> getAlert() {
        return Optional.ofNullable(this.alert);
    }
}
