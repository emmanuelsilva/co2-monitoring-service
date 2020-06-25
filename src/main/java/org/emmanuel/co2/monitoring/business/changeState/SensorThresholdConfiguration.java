package org.emmanuel.co2.monitoring.business.changeState;

public enum SensorThresholdConfiguration {

    THRESHOLD(2000),
    MAX_ATTEMPTS(3);

    private int value;

    SensorThresholdConfiguration(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}