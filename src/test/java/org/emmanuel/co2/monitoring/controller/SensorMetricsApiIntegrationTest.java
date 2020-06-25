package org.emmanuel.co2.monitoring.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorMetricsApiIntegrationTest extends SensorApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Override
    public MockMvc getMockMvc() {
        return mockMvc;
    }


    @Test
    void shouldReturnZeroWhenThereIsNoSensorMetrics() throws Exception {
        var sensorId = givenSensorId();
        assertThatSensorMetricsAre(sensorId, 0, 0D);
    }

    @Test
    void shouldReturnSensorMetric() throws Exception {
        var sensorId = givenSensorId();
        registerMeasurement(sensorId, 10);
        registerMeasurement(sensorId, 200);
        registerMeasurement(sensorId, 22);

        assertThatSensorMetricsAre(sensorId, 200, 77.33);
    }

    void assertThatSensorMetricsAre(String sensorId, int max, double average) throws Exception {
        MockHttpServletRequestBuilder getStatusRequest = createMetricRequest(sensorId);

        getMockMvc().perform(getStatusRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.maxLast30Days").value(max))
                .andExpect(jsonPath("$.avgLast30Days").value(average));
    }

    MockHttpServletRequestBuilder createMetricRequest(String sensorId) {
        return get(API_V1_PATH + "/{sensorId}/metrics", sensorId);
    }
}
