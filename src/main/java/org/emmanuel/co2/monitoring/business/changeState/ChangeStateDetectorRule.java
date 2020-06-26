package org.emmanuel.co2.monitoring.business.changeState;

import lombok.RequiredArgsConstructor;
import org.emmanuel.co2.monitoring.domain.entity.CurrentSensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Builder approach to writing sensor condition rules to easily write conditions.
 */
@RequiredArgsConstructor
public class ChangeStateDetectorRule {

    private final List<BiPredicate<CurrentSensorState, SensorMeasurement>> conditions;

    public boolean match(CurrentSensorState currentSensorState, SensorMeasurement measurement) {
        return this.conditions
                .stream()
                .allMatch(condition -> condition.test(currentSensorState, measurement));
    }

    public static class Builder {

        private List<BiPredicate<CurrentSensorState, SensorMeasurement>> conditions;

        public Builder() {
            this.conditions = new ArrayList<>();
        }

        public ChangeStateDetectorRule build() {
            return new ChangeStateDetectorRule(this.conditions);
        }

        public Builder whenStateIs(SensorState state) {
            BiPredicate<CurrentSensorState, SensorMeasurement> stateFilter = (currentState, measurement) ->
                    currentState.getState().equals(state);

            this.conditions.add(stateFilter);
            return this;
        }

        public Builder measurementIsAbove(int threshold) {
            BiPredicate<CurrentSensorState, SensorMeasurement> thresholdAboveFilter = (currentState, measurement) ->
                    measurement.getValue() > threshold;

            this.conditions.add(thresholdAboveFilter);
            return this;
        }

        public Builder measurementIsBelow(int threshold) {
            BiPredicate<CurrentSensorState, SensorMeasurement> thresholdAboveFilter = (currentState, measurement) ->
                    measurement.getValue() < threshold;

            this.conditions.add(thresholdAboveFilter);
            return this;
        }

        public Builder withWarningAttempts(int warningAttempts) {
            BiPredicate<CurrentSensorState, SensorMeasurement> warningAttemptFilter = (currentState, measurement) ->
                    currentState
                            .getWarning()
                            .map(w -> w.getHigherReads().size() == warningAttempts)
                            .orElse(false);

            this.conditions.add(warningAttemptFilter);
            return this;
        }

        public Builder withAlertLowerAttempts(int lowerAttempts) {
            BiPredicate<CurrentSensorState, SensorMeasurement> lowerErrorAttemptsFilter = (currentState, measurement) ->
                    currentState.getAlert()
                            .map(a -> a.getLowerReads().size() <= lowerAttempts)
                            .orElse(false);

            this.conditions.add(lowerErrorAttemptsFilter);

            return this;
        }
    }


}
