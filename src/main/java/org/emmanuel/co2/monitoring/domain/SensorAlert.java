package org.emmanuel.co2.monitoring.domain;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class SensorAlert {

    private final OffsetDateTime startTime;
    private final OffsetDateTime endTime;
    private final int measurement1;
    private final int measurement2;
    private final int measurement3;

}
