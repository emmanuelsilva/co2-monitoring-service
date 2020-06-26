package org.emmanuel.co2.monitoring.business.changeState.warning;

import org.emmanuel.co2.monitoring.business.changeState.ChangeState;
import org.emmanuel.co2.monitoring.business.changeState.ChangeStateDetectorRule;
import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

public class IncrementWarningAttemptChangeState implements ChangeState {

    private final static int WARN_THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();

    @Override
    public ChangeStateDetectorRule rule() {
        return new ChangeStateDetectorRule.Builder()
                .whenStateIs(SensorState.WARN)
                .measurementIsAbove(WARN_THRESHOLD)
                .withWarningAttempts(1)
                .build();
    }

    @Override
    public CurrentSensorState changeState(CurrentSensorState currentState, SensorMeasurement measurement) {
        var sensor = currentState.getSensor();
        var warning = currentState.getWarning().orElseThrow(IllegalStateException::new);

        var incrementedWarning = SensorWarning.copy(warning);
        incrementedWarning.addHigherRead(measurement);

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.WARN)
                .warning(incrementedWarning)
                .build();

    }
}
