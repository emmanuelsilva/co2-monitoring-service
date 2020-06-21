package org.emmanuel.co2.monitoring.dto;

import lombok.Builder;
import lombok.Value;

import java.time.OffsetDateTime;

@Value
@Builder
public class SensorMeasurementRequest {
    private String sensorId;
    private int value;
    private OffsetDateTime time;
}
