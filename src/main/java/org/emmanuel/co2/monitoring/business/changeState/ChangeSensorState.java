package org.emmanuel.co2.monitoring.business.changeState;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.vo.CurrentSensorState;

@RequiredArgsConstructor
public class ChangeSensorState {

    private final static int THRESHOLD = SensorThresholdConfiguration.THRESHOLD.value();
    private final static int MAX_READ_ATTEMPTS = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

    private final CurrentSensorState currentState;
    private final SensorMeasurement measurement;

    public CurrentSensorState change() {
        return switch (currentState.getState()) {
            case OK -> handleChangeWhenStateIsOK();
            case WARN -> handleChangeWhenStateIsWarning();
            case ALERT -> handleChangeWhenStateIsAlert();
        };
    }

    private CurrentSensorState handleChangeWhenStateIsOK() {

        if (isLowerThanThreshold(measurement)) {
            return currentState;
        }

        return turnSensorToWarningState(measurement);
    }

    private CurrentSensorState handleChangeWhenStateIsWarning() {
        var sensor = currentState.getSensor();
        var currentWarning = currentState.getWarning().orElseThrow(IllegalStateException::new);

        if (isLowerThanThreshold(measurement)) {
            return turnSensorToOKState(currentWarning, measurement);
        }

        var changedWarning = SensorWarning.copy(currentWarning);
        changedWarning.addHigherRead(measurement);

        if (reachedMaxAllowedHigherReads(changedWarning)) {
            return turnSensorToAlertState(changedWarning, measurement);
        }

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.WARN)
                .warning(changedWarning)
                .build();
    }

    private CurrentSensorState handleChangeWhenStateIsAlert() {
        var sensor = currentState.getSensor();
        var currentAlert = currentState.getAlert().orElseThrow(IllegalStateException::new);
        var changedAlert = SensorAlert.from(currentAlert);

        if (isLowerThanThreshold(measurement)) {
            changedAlert.addLowerRead(measurement);
        } else {
            changedAlert.addHigherRead(measurement);
        }

        if (canReturnSensorToOKState(changedAlert)) {
            return turnSensorToOKState(changedAlert, measurement);
        }

        return CurrentSensorState.builder()
                .sensor(sensor)
                .state(SensorState.ALERT)
                .alert(changedAlert)
                .build();
    }

    private boolean isLowerThanThreshold(SensorMeasurement measurement) {
        return measurement.getValue() < THRESHOLD;
    }

    private boolean reachedMaxAllowedHigherReads(SensorWarning warning) {
        return warning.getHigherReads().size() == MAX_READ_ATTEMPTS;
    }

    private boolean canReturnSensorToOKState(SensorWarning warning) {
        return warning.getLowerReads().size() == MAX_READ_ATTEMPTS;
    }

    private CurrentSensorState turnSensorToOKState(SensorAlert alert, SensorMeasurement measurement) {
        var solvedAlert = SensorAlert.solve(alert, measurement.getTimestamp());

        return CurrentSensorState.builder()
                .sensor(alert.getSensor())
                .state(SensorState.OK)
                .alert(solvedAlert)
                .build();
    }

    private CurrentSensorState turnSensorToOKState(SensorWarning currentWarning, SensorMeasurement measurement) {
        var solvedWarning = SensorWarning.end(currentWarning, measurement.getTimestamp());
        solvedWarning.addLowerRead(measurement);

        return CurrentSensorState.builder()
                .sensor(currentWarning.getSensor())
                .state(SensorState.OK)
                .warning(solvedWarning)
                .build();
    }

    private CurrentSensorState turnSensorToWarningState(SensorMeasurement measurement) {
        var warning = SensorWarning.create(measurement.getSensor(), measurement.getTimestamp());
        warning.addHigherRead(measurement);

        return CurrentSensorState.builder()
                .sensor(measurement.getSensor())
                .state(SensorState.WARN)
                .warning(warning)
                .build();
    }

    private CurrentSensorState turnSensorToAlertState(SensorWarning changedWarning, SensorMeasurement measurement) {
        var finishedWarning = SensorWarning.end(changedWarning, measurement.getTimestamp());
        var alert = SensorAlert.from(finishedWarning);

        return CurrentSensorState.builder()
                .sensor(changedWarning.getSensor())
                .state(SensorState.ALERT)
                .warning(finishedWarning)
                .alert(alert)
                .build();
    }
}
