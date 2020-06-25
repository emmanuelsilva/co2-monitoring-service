package org.emmanuel.co2.monitoring.controller;

import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorStatusApiIntegrationTest extends SensorApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Override
    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Test
    void shouldReturn404WhenThereIsNoSensor() throws Exception {
        var sensorId = givenSensorId();
        assertThatSensorStatusIsNotFounded(sensorId);
    }

    @Test
    void shouldRegisterMeasurement() throws Exception {
        var sensorId = givenSensorId();
        registerMeasurement(sensorId, 0);
        assertThatSensorStatusIs(sensorId, "OK");
    }

    @Test
    void shouldChangeSensorStatusToWarning() throws Exception {
        var sensorId = givenSensorId();
        var aboveThresholdValue = SensorThresholdConfiguration.THRESHOLD.value() + 1;
        registerMeasurement(sensorId, aboveThresholdValue);
        assertThatSensorStatusIs(sensorId, "WARN");
    }

    @Test
    void shouldChangeSensorStatusToOKAfterWarning() throws Exception {
        var sensorId = givenSensorId();
        var aboveThresholdValue = SensorThresholdConfiguration.THRESHOLD.value() + 1;

        registerMeasurement(sensorId, aboveThresholdValue);
        assertThatSensorStatusIs(sensorId, "WARN");

        var belowThresholdValue = SensorThresholdConfiguration.THRESHOLD.value() -100;
        registerMeasurement(sensorId, belowThresholdValue);
        assertThatSensorStatusIs(sensorId, "OK");
    }

    @Test
    void shouldChangeSensorToAlert() throws Exception {
        var sensorId = givenSensorId();
        changeSensorToAlertState(sensorId);
        assertThatSensorStatusIs(sensorId, "ALERT");
    }

    @Test
    void shouldChangeSensorToOKAfterAlert() throws Exception {
        var sensorId = givenSensorId();
        changeSensorToAlertState(sensorId);
        assertThatSensorStatusIs(sensorId, "ALERT");

        changeSensorToOkState(sensorId);
        assertThatSensorStatusIs(sensorId, "OK");
    }
}
