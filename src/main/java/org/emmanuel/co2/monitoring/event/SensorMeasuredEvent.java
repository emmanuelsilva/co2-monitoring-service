package org.emmanuel.co2.monitoring.event;

import lombok.Value;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

@Value
public class SensorMeasuredEvent {
    private final SensorMeasurement measurement;
}
