package org.emmanuel.co2.monitoring.business.stateRule;

import org.emmanuel.co2.monitoring.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class IncrementWarningReadingSensorStateRuleTest extends BaseSensorStateRuleTestCase {

    private IncrementWarningReadingSensorStateRule rule;

    @BeforeEach
    void setUp() {
        this.rule = new IncrementWarningReadingSensorStateRule();
    }

    @Override
    SensorState getState() {
        return SensorState.WARN;
    }

    @Override
    public IncrementWarningReadingSensorStateRule getRule() {
        return rule;
    }

    @Test
    void shouldAcceptWhenStateIsWarningAndNotReachMaxWarnAttempts() {
        var sensor = new Sensor("123");
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        var warnState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertTrue(accepted);
    }

    @Test
    void shouldNotAcceptWhenStateIsWarningButReachMaxWarnAttempts() {
        var sensor = new Sensor("123");
        var warnState = givenWarningWithReachMaxWarnAttempt(sensor);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var accepted = rule.accept(warnState, warningMeasurement);
        assertFalse(accepted);
    }

    @Test
    void shouldIncrementWarningAttempts() {
        var sensor = new Sensor("123");
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        warning.addWarningRead(new SensorMeasurement(sensor,  5000, OffsetDateTime.now()));

        var warnState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        var warningMeasurement = getHigherThresholdMeasurement(sensor);

        var result = rule.defineState(warnState, warningMeasurement);

        assertThatWarningWasIncremented(result);
    }

    private void assertThatWarningWasIncremented(CurrentSensorState result) {
        assertNotNull(result);
        assertEquals(SensorState.WARN, result.getState());

        var warningOpt = result.getWarning();
        assertTrue(warningOpt.isPresent());
        assertEquals(2, warningOpt.get().getWarningReads().size());
    }

    private CurrentSensorState givenWarningWithReachMaxWarnAttempt(Sensor sensor) {
        var now = OffsetDateTime.now();
        var warning = SensorWarning.create(sensor, now);

        IntStream.rangeClosed(1, SensorThresholdConfiguration.MAX_ATTEMPTS.value())
                .forEach(i -> warning.addWarningRead(new SensorMeasurement(sensor, i, now)));

        return new CurrentSensorState(sensor, SensorState.WARN, warning);
    }

}