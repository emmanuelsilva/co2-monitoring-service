package org.emmanuel.co2.monitoring.controller;

import org.emmanuel.co2.monitoring.business.changeState.SensorThresholdConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.OffsetDateTime;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

abstract class SensorApiIntegrationTest {

    static final String API_V1_PATH = "/api/v1/sensors/";

    abstract MockMvc getMockMvc();

    void changeSensorToAlertState(String sensorId) {
        var aboveThresholdValue = SensorThresholdConfiguration.THRESHOLD.value() + 1;
        var alertAttemptsCount = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

        IntStream.rangeClosed(1, alertAttemptsCount)
                .forEach(i -> registerMeasurement(sensorId, aboveThresholdValue));
    }

    void changeSensorToOkState(String sensorId) {
        var belowThresholdValue = SensorThresholdConfiguration.THRESHOLD.value() - 10;
        var alertAttemptsCount = SensorThresholdConfiguration.MAX_ATTEMPTS.value();

        IntStream.rangeClosed(1, alertAttemptsCount)
                .forEach(i -> registerMeasurement(sensorId, belowThresholdValue));
    }

    void registerMeasurement(String sensorId, int value) {
        var measurementBody = buildMeasurementRequest(value);
        var measurementRequest = createMeasurementRequest(sensorId, measurementBody);

        try {
            getMockMvc().perform(measurementRequest)
                    .andExpect(status().isOk());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    void assertThatSensorStatusIs(String sensorId, String expectedStatus) throws Exception {
        MockHttpServletRequestBuilder getStatusRequest = createStatusRequest(sensorId);

        getMockMvc().perform(getStatusRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(expectedStatus));
    }

    void assertThatSensorStatusIsNotFounded(String sensorId) throws Exception {
        MockHttpServletRequestBuilder getStatusRequest = createStatusRequest(sensorId);

        getMockMvc().perform(getStatusRequest)
                .andExpect(status().isNotFound());
    }

    MockHttpServletRequestBuilder createStatusRequest(String sensorId) {
        return get(API_V1_PATH + "/{sensorId}", sensorId);
    }

    MockHttpServletRequestBuilder createMeasurementRequest(String sensorId, String body) {
        return post(API_V1_PATH + "/{sensorId}/mesurements", sensorId)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);
    }

    String buildMeasurementRequest(int value) {
        var now = OffsetDateTime.now();
        return "{\"co2\": "+ value +",\"time\": \""+ now + "\"}";
    }

    String givenSensorId() {
        return UUID.randomUUID().toString();
    }
}
