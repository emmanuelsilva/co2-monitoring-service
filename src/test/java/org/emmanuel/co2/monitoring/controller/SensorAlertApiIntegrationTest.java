package org.emmanuel.co2.monitoring.controller;

import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SensorAlertApiIntegrationTest extends SensorApiIntegrationTest {

    private static final DateTimeFormatter EXPECTED_TIME_FORMATTER = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'hh:mm:ssX");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SensorAlertRepository alertRepository;

    @Override
    public MockMvc getMockMvc() {
        return mockMvc;
    }

    @Test
    void shouldReturnOpenedAlert() throws Exception {
        var sensorId = givenSensorId();
        super.changeSensorToAlertState(sensorId);
        assertThatOneAlertOpenedWasReturned(sensorId);
    }

    @Test
    void shouldReturnClosedAlert() throws Exception {
        var sensorId = givenSensorId();
        super.changeSensorToAlertState(sensorId);
        super.changeSensorToOkState(sensorId);
        assertThatOneAlertClosedWasReturned(sensorId);
    }

    void assertThatOneAlertOpenedWasReturned(String sensorId) throws Exception {
        MockHttpServletRequestBuilder getStatusRequest = createAlertRequest(sensorId);
        var alert = alertRepository.findActiveBySensorId(sensorId).get();
        var startAt = alert.getStartAt();

        getMockMvc().perform(getStatusRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startTime", is(startAt.format(EXPECTED_TIME_FORMATTER))))
                .andExpect(jsonPath("$[0].endTime").value(IsNull.nullValue()));
    }

    void assertThatOneAlertClosedWasReturned(String sensorId) throws Exception {
        MockHttpServletRequestBuilder getStatusRequest = createAlertRequest(sensorId);
        var alert = alertRepository.findAllBySensorId(sensorId).stream().findFirst().get();

        var startAt = alert.getStartAt();
        var endAt = alert.getEndAt();

        getMockMvc().perform(getStatusRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startTime", is(startAt.format(EXPECTED_TIME_FORMATTER))))
                .andExpect(jsonPath("$[0].endTime", is(endAt.format(EXPECTED_TIME_FORMATTER))));
    }

    MockHttpServletRequestBuilder createAlertRequest(String sensorId) {
        return get(API_V1_PATH + "/{sensorId}/alerts", sensorId);
    }
}
