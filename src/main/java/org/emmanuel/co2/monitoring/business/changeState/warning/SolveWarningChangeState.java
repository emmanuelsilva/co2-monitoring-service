package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

public class SolveWarningChangeState implements ChangeState {

    private final static int WARN_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.WARN)
                .measurementIsBelow(WARN_THRESHOLD)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        var sensor = currentSensorState.getSensor();
        var warning = currentSensorState.getWarning().orElseThrow(IllegalStateException::new);
        var solvedWarning = SensorWarning.end(warning, measurement.getTimestamp());

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.OK)
                .warning(solvedWarning)
                .build();
    }
}
