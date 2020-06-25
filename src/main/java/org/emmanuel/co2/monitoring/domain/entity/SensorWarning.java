package org.emmanuel.co2.monitoring.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode(exclude = {"endAt", "higherReads", "lowerReads"})
@RequiredArgsConstructor
public class SensorWarning implements HasSensor {

    private final Sensor sensor;
    private final OffsetDateTime startAt;
    private final OffsetDateTime endAt;
    private final List<Integer> higherReads;
    private final List<Integer> lowerReads;

    public void addHigherRead(SensorMeasurement measurement) {
        this.higherReads.add(measurement.getValue());
    }

    public void addLowerRead(SensorMeasurement measurement) {
        this.lowerReads.add(measurement.getValue());
    }

    public boolean isOpened() {
        return endAt == null;
    }

    public static SensorWarning create(Sensor sensor, OffsetDateTime startAt) {
        return new SensorWarning(sensor, startAt, null, new ArrayList<>(), new ArrayList<>());
    }

    public static SensorWarning copy(SensorWarning warning) {
        return new SensorWarning(
                warning.getSensor(),
                warning.getStartAt(),
                warning.getEndAt(),
                new ArrayList<>(warning.getHigherReads()),
                new ArrayList<>(warning.getLowerReads()));
    }

    public static SensorWarning create(Sensor sensor, OffsetDateTime startAt, OffsetDateTime endAt) {
        return new SensorWarning(sensor, startAt, endAt, Collections.emptyList(), Collections.emptyList());
    }

    public static SensorWarning end(SensorWarning warning, OffsetDateTime endAt) {
        return new SensorWarning(
                warning.getSensor(),
                warning.getStartAt(),
                endAt,
                warning.getHigherReads(),
                warning.getLowerReads());
    }
}
