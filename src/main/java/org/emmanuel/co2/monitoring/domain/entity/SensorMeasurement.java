package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class SensorMeasurement {
    private final Sensor sensor;
    private final int value;
    private final OffsetDateTime timestamp;
}
