package org.emmanuel.co2.monitoring.domain.entity;

import lombok.Value;

@Value
public class SensorMetric {
    private final int max;
    private final double average;
}
