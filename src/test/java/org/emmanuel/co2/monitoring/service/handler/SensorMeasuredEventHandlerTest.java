package org.emmanuel.co2.monitoring.service.handler;

import org.emmanuel.co2.monitoring.business.stateRule.SensorStateRule;
import org.emmanuel.co2.monitoring.domain.entity.*;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.*;

class SensorMeasuredEventHandlerTest {

    private SensorWarningRepository sensorWarningRepository;
    private SensorAlertRepository sensorAlertRepository;
    private SensorStateRule sensorStateRule;
    private SensorMeasuredEventHandler eventHandler;

    @BeforeEach
    void setUp() {
        this.sensorWarningRepository = mock(SensorWarningRepository.class);
        this.sensorAlertRepository = mock(SensorAlertRepository.class);
        this.sensorStateRule = mock(SensorStateRule.class);
        this.eventHandler = new SensorMeasuredEventHandler(
                this.sensorWarningRepository,
                this.sensorAlertRepository,
                Arrays.asList(sensorStateRule));
    }

    @Test
    void shouldSaveWarningStateWhenHasWarningChanges() {
        var sensor = new Sensor("123");
        var currentState = new CurrentSensorState(sensor, SensorState.OK);
        var warningMeasurement = new SensorMeasurement(sensor, 3000, OffsetDateTime.now());
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());

        givenWarnMeasuredState(sensor, currentState, warningMeasurement, warning);
        this.eventHandler.handleMeasuredEvent(new SensorMeasuredEvent(warningMeasurement));

        assertThatWarningWasSavedOnDatabase(warning);
    }

    @Test
    void shouldSaveAlertStateWhenHasAlertChanges() {
        var sensor = new Sensor("123");
        var higherMeasurement = new SensorMeasurement(sensor, 3000, OffsetDateTime.now());
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        var alert = SensorAlert.from(warning);
        var currentState = new CurrentSensorState(sensor, SensorState.OK);

        givenAlertMeasuredState(sensor, currentState, higherMeasurement, warning, alert);
        this.eventHandler.handleMeasuredEvent(new SensorMeasuredEvent(higherMeasurement));

        assertThatAlertWasSavedOnDatabase(alert);
    }

    private void assertThatWarningWasSavedOnDatabase(SensorWarning warning) {
        verify(sensorWarningRepository).save(warning);
    }

    private void assertThatAlertWasSavedOnDatabase(SensorAlert alert) {
        verify(sensorAlertRepository).save(alert);
    }

    private void givenWarnMeasuredState(Sensor sensor,
                                        CurrentSensorState currentState,
                                        SensorMeasurement warningMeasurement,
                                        SensorWarning warning) {

        var warningState = new CurrentSensorState(sensor, SensorState.WARN, warning);
        when(sensorStateRule.accept(currentState, warningMeasurement)).thenReturn(true);
        when(sensorStateRule.defineState(currentState, warningMeasurement)).thenReturn(warningState);
    }

    private void givenAlertMeasuredState(Sensor sensor,
                                        CurrentSensorState currentState,
                                        SensorMeasurement higherMeasurement,
                                        SensorWarning warning,
                                        SensorAlert alert) {

        var alertState = new CurrentSensorState(sensor, SensorState.ALERT, warning, alert);
        when(sensorStateRule.accept(currentState, higherMeasurement)).thenReturn(true);
        when(sensorStateRule.defineState(currentState, higherMeasurement)).thenReturn(alertState);
    }
}