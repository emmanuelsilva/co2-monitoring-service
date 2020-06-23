package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class SensorMeasurement implements HasSensor {

    private final Sensor sensor;
    private final int value;
    private final OffsetDateTime timestamp;
}
