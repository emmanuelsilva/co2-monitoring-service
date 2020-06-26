package org.emmanuel.co2.monitoring.domain.vo;

import lombok.Builder;
import lombok.Value;
import org.emmanuel.co2.monitoring.domain.entity.*;

import java.util.Optional;

@Value
@Builder
/**
 * A Value Object is an immutable type that is distinguishable only by the state of its properties.
 **/
public class CurrentSensorState implements HasSensor {

    private final Sensor sensor;
    private final SensorState state;
    private final SensorWarning warning;
    private final SensorAlert alert;

    public Optional<SensorWarning> getWarning() {
        return Optional.ofNullable(this.warning);
    }

    public Optional<SensorAlert> getAlert() {
        return Optional.ofNullable(this.alert);
    }
}
