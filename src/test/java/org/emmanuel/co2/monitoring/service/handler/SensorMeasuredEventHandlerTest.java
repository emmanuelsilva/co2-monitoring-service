package org.emmanuel.co2.monitoring.service.handler;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.event.SensorMeasuredEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class SensorMeasuredEventHandlerTest {

    private SensorWarningRepository sensorWarningRepository;
    private SensorAlertRepository sensorAlertRepository;
    private SensorMeasuredEventHandler eventHandler;

    @BeforeEach
    void setUp() {
        this.sensorWarningRepository = mock(SensorWarningRepository.class);
        this.sensorAlertRepository = mock(SensorAlertRepository.class);
        this.eventHandler = new SensorMeasuredEventHandler(
                this.sensorWarningRepository,
                this.sensorAlertRepository);
    }

    @Test
    void shouldSaveWarningStateWhenHasWarningChanges() {
        var sensor = new Sensor("123");
        var warningMeasurement = new SensorMeasurement(sensor, 3000, OffsetDateTime.now());

        givenNoWarningState(sensor);
        this.eventHandler.handleMeasuredEvent(new SensorMeasuredEvent(warningMeasurement));

        assertThatWarningWasSavedOnDatabase(sensor);
    }

    @Test
    void shouldSaveAlertStateWhenHasAlertChanges() {
        var sensor = new Sensor("123");
        var higherMeasurement = new SensorMeasurement(sensor, 3000, OffsetDateTime.now());
        givenAlertOnDatabaseForSensor(sensor);

        this.eventHandler.handleMeasuredEvent(new SensorMeasuredEvent(higherMeasurement));

        assertThatAlertWasSavedOnDatabase(sensor);
    }

    private void givenAlertOnDatabaseForSensor(Sensor sensor) {
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        var alert = SensorAlert.from(warning);

        when(this.sensorAlertRepository.findActiveBySensorId(sensor.getId())).thenReturn(Optional.of(alert));
    }

    private void givenNoWarningState(Sensor sensor) {
        when(sensorWarningRepository.findActiveBySensorId(sensor.getId()))
                .thenReturn(Optional.empty());
    }

    private void assertThatWarningWasSavedOnDatabase(Sensor sensor) {
        var saveWarningCaptor = ArgumentCaptor.forClass(SensorWarning.class);
        verify(sensorWarningRepository).save(saveWarningCaptor.capture());

        var savedWarning = saveWarningCaptor.getValue();
        assertEquals(sensor, savedWarning.getSensor());
        assertNull(savedWarning.getEndAt());
    }

    private void assertThatAlertWasSavedOnDatabase(Sensor sensor) {
        var saveAlertCaptor = ArgumentCaptor.forClass(SensorAlert.class);
        verify(sensorAlertRepository).save(saveAlertCaptor.capture());

        var savedAlert = saveAlertCaptor.getValue();
        assertEquals(sensor, savedAlert.getSensor());
        assertNull(savedAlert.getEndAt());
    }

}