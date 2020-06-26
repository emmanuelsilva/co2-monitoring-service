package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

public class WarnChangeState implements ChangeState {

    private final static int WARN_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.OK)
                .measurementIsAbove(WARN_THRESHOLD)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = SensorWarning.create(sensor, measurement.getTimestamp());
        warning.addHigherRead(measurement);

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.WARN)
                .warning(warning)
                .build();
    }
}
