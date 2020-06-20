package org.emmanuel.co2.monitoring.domain;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class SensorMeasurement {

    private final int value;

    /**
     * OffsetDateTime was chosen to persist time-zone offset as well.
     **/
    private final OffsetDateTime timestamp;

}
