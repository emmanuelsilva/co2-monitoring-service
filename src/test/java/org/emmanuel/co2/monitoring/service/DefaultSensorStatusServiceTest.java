package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.domain.entity.SensorWarning;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorRepository;
import org.emmanuel.co2.monitoring.domain.repository.SensorWarningRepository;
import org.emmanuel.co2.monitoring.excpetion.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultSensorStatusServiceTest {

    private SensorRepository sensorRepository;
    private SensorWarningRepository sensorWarningRepository;
    private SensorAlertRepository sensorAlertRepository;
    private DefaultSensorStatusService statusService;

    @BeforeEach
    void setUp() {
        this.sensorRepository = mock(SensorRepository.class);
        this.sensorWarningRepository = mock(SensorWarningRepository.class);
        this.sensorAlertRepository = mock(SensorAlertRepository.class);
        this.statusService = new DefaultSensorStatusService(sensorRepository, sensorWarningRepository, sensorAlertRepository);
    }

    @Test
    void shouldFailsWhenSensorNotFound() {
        String sensorId = "123";
        when(this.sensorRepository.findById(sensorId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> this.statusService.getCurrentState(sensorId));
    }

    @Test
    void shouldReturnOKStateWhenThereIsNoWarning() {
        var sensor = new Sensor("123");
        givenExistentSensor(sensor);
        givenNonExistentWarning(sensor);

        var status = this.statusService.getCurrentState(sensor.getId());
        assertEquals(SensorState.OK, status);
    }

    @Test
    void shouldReturnWarnStateWhenThereIsWarning() {
        var sensor = new Sensor("123");
        givenExistentSensor(sensor);
        givenExistentWarning(sensor);

        var status = this.statusService.getCurrentState(sensor.getId());
        assertEquals(SensorState.WARN, status);
    }

    @Test
    void shouldReturnAlertStateWhenThereIsAlert() {
        var sensor = new Sensor("123");
        givenExistentSensor(sensor);
        givenExistentWarning(sensor);
        givenExistentAlert(sensor);

        var status = this.statusService.getCurrentState(sensor.getId());
        assertEquals(SensorState.ALERT, status);
    }

    private void givenNonExistentWarning(Sensor sensor) {
        when(this.sensorWarningRepository.findActiveBySensorId(sensor.getId())).thenReturn(Optional.empty());
    }

    private void givenExistentWarning(Sensor sensor) {
        var warning = SensorWarning.create(sensor, OffsetDateTime.now());
        when(this.sensorWarningRepository.findActiveBySensorId(sensor.getId())).thenReturn(Optional.of(warning));
    }

    private void givenExistentSensor(Sensor sensor) {
        when(this.sensorRepository.findById(sensor.getId())).thenReturn(Optional.of(sensor));
    }

    private void givenExistentAlert(Sensor sensor) {
        var now = OffsetDateTime.now();
        var alert = new SensorAlert(sensor, now, null, Collections.emptyList());
        when(this.sensorAlertRepository.findActiveBySensorId(sensor.getId())).thenReturn(Optional.of(alert));
    }
}