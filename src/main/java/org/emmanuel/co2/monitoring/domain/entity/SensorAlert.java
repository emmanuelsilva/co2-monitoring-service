package org.emmanuel.co2.monitoring.domain.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SensorAlert extends SensorWarning {

    public SensorAlert(Sensor sensor, OffsetDateTime startAt, OffsetDateTime endAt, List<Integer> higherReads) {
        super(sensor, startAt, endAt, higherReads);
    }

    public static SensorAlert from(SensorWarning warning) {
        return new SensorAlert(warning.getSensor(), warning.getStartAt(), null, new ArrayList<>(warning.getHigherReads()));
    }
}
