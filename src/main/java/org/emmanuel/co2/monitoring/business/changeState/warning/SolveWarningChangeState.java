package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;

public class SolveWarningChangeState implements ChangeState {

    private final static int WARN_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return ChangeStateDetectorRule
                .start()
                .whenStateIs(SensorState.WARN)
                .measurementIsBelow(WARN_THRESHOLD);
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        var sensor = currentSensorState.getSensor();
        var warning = currentSensorState.getWarning().orElseThrow(IllegalStateException::new);
        var solvedWarning = SensorWarning.end(warning, measurement.getTimestamp());

        return new CurrentSensorState(sensor, SensorState.OK, solvedWarning);
    }
}
