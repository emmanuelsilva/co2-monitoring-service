package org.emmanuel.co2.monitoring.service.handler;

import org.emmanuel.co2.monitoring.business.stateRule.SensorStateRule;
import org.emmanuel.co2.monitoring.business.stateRule.WarningDetectedSensorStateRule;
import org.emmanuel.co2.monitoring.domain.entity.*;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SensorMeasuredEventHandlerTest {

    private SensorWarningRepository sensorWarningRepository;
    private SensorStateRule sensorStateRule;
    private SensorMeasuredEventHandler eventHandler;

    @BeforeEach
    void setUp() {
        this.sensorWarningRepository = mock(SensorWarningRepository.class);
        this.sensorStateRule = mock(SensorStateRule.class);
        this.eventHandler = new SensorMeasuredEventHandler(
                this.sensorWarningRepository,
                Arrays.asList(sensorStateRule));
    }

    @Test
    void shouldSaveWarningState() {
        var sensor = new Sensor("123");
        var currentState = new CurrentSensorState(sensor, SensorState.OK);
        var warningMeasurement = new SensorMeasurement(sensor, 3000, OffsetDateTime.now());
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());

        givenWarnMeasuredState(sensor, currentState, warningMeasurement, warning);
        this.eventHandler.handleMeasuredEvent(new SensorMeasuredEvent(warningMeasurement));

        assertThatWarningWasSavedOnDatabase(warning);
    }

    private void assertThatWarningWasSavedOnDatabase(SensorWarning warning) {
        verify(sensorWarningRepository).save(warning);
    }

    private void givenWarnMeasuredState(Sensor sensor,
                                        CurrentSensorState currentState,
                                        SensorMeasurement warningMeasurement,
                                        SensorWarning warning) {

        var warningState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        when(sensorStateRule.accept(currentState, warningMeasurement)).thenReturn(true);
        when(sensorStateRule.defineState(currentState, warningMeasurement)).thenReturn(warningState);
    }
}