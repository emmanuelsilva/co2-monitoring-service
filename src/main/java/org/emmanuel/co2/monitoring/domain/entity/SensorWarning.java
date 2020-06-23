package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Value;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Value
public class SensorWarning {

    private final Sensor sensor;
    private final OffsetDateTime startAt;
    private final OffsetDateTime endAt;
    private final List<Integer> warningReads;

    public void addWarningRead(SensorMeasurement measurement) {
        this.warningReads.add(measurement.getValue());
    }

    public static SensorWarning create(Sensor sensor, OffsetDateTime startAt) {
        return new SensorWarning(sensor, startAt, null, new ArrayList<>());
    }

    public static SensorWarning copy(SensorWarning warning) {
        return new SensorWarning(warning.getSensor(), warning.getStartAt(), warning.getEndAt(), warning.getWarningReads());
    }
}
