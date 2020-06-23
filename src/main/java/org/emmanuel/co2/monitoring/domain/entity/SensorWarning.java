package org.emmanuel.co2.monitoring.domain.entity;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class SensorWarning {

    private final Sensor sensor;
    private final OffsetDateTime startAt;
    private final OffsetDateTime endAt;
    private final List<Integer> higherReads;

    public void addHigherRead(SensorMeasurement measurement) {
        this.higherReads.add(measurement.getValue());
    }

    public boolean isOpened() {
        return endAt == null;
    }

    public static SensorWarning create(Sensor sensor, OffsetDateTime startAt) {
        return new SensorWarning(sensor, startAt, null, new ArrayList<>());
    }

    public static SensorWarning copy(SensorWarning warning) {
        return new SensorWarning(warning.getSensor(), warning.getStartAt(), warning.getEndAt(), warning.getHigherReads());
    }

    public static SensorWarning end(SensorWarning warning, OffsetDateTime endAt) {
        return new SensorWarning(warning.getSensor(), warning.getStartAt(), endAt, warning.getHigherReads());
    }
}
