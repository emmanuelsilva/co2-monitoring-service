package org.emmanuel.co2.monitoring.business.changeState;

import org.emmanuel.co2.monitoring.business.changeState.alert.AlertChangeState;
import org.emmanuel.co2.monitoring.business.changeState.alert.IncrementAlertLowerReadingChangeState;
import org.emmanuel.co2.monitoring.business.changeState.alert.SolveAlertChangeState;
import org.emmanuel.co2.monitoring.business.changeState.warning.IncrementWarningAttemptChangeState;
import org.emmanuel.co2.monitoring.business.changeState.warning.SolveWarningChangeState;
import org.emmanuel.co2.monitoring.business.changeState.warning.WarnChangeState;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;

import java.util.ArrayList;
import java.util.List;

public class ChangeSensorState {

    private List<ChangeState> changeStates;

    public ChangeSensorState() {
        this.changeStates = new ArrayList<>();
        this.changeStates.add(new WarnChangeState());
        this.changeStates.add(new IncrementWarningAttemptChangeState());
        this.changeStates.add(new SolveWarningChangeState());
        this.changeStates.add(new AlertChangeState());
        this.changeStates.add(new IncrementAlertLowerReadingChangeState());
        this.changeStates.add(new SolveAlertChangeState());
    }

    public CurrentSensorState change(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        var sensorStateChange = getSensorChangeState(currentSensorState, measurement);
        return sensorStateChange.changeState(currentSensorState, measurement);
    }

    private ChangeState getSensorChangeState(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        return this.changeStates
                .stream()
                .filter(sensorChangeState -> sensorChangeState.rule().match(currentSensorState, measurement))
                .findFirst()
                .orElse(new NoChangeState());
    }

}
