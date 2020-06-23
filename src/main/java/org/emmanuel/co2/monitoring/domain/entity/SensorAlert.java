package org.emmanuel.co2.monitoring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SensorAlert extends SensorWarning {

    public SensorAlert(Sensor sensor,
                       OffsetDateTime startAt,
                       OffsetDateTime endAt,
                       List<Integer> higherReads,
                       List<Integer> lowerReads) {
        super(sensor, startAt, endAt, higherReads, lowerReads);
    }

    public static SensorAlert create(Sensor sensor, OffsetDateTime startAt) {
        return new SensorAlert(sensor, startAt, null, Collections.emptyList(), Collections.emptyList());
    }

    public static SensorAlert create(Sensor sensor, OffsetDateTime startAt, OffsetDateTime endAt) {
        return new SensorAlert(sensor, startAt, endAt, Collections.emptyList(), Collections.emptyList());
    }

    public static SensorAlert end(SensorAlert alert, OffsetDateTime endAt) {
        return new SensorAlert(
                alert.getSensor(),
                alert.getStartAt(),
                endAt,
                alert.getHigherReads(),
                alert.getLowerReads());
    }

    public static SensorAlert from(SensorWarning warning) {
        return new SensorAlert(
                warning.getSensor(),
                warning.getStartAt(),
                null,
                new ArrayList<>(warning.getHigherReads()),
                new ArrayList<>());
    }
}
