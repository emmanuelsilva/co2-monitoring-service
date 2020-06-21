package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorMeasurement;
import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;
import org.emmanuel.co2.monitoring.domain.repository.SensorMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultSensorMetricServiceTest {

    private SensorMeasurementRepository sensorMeasurementRepository;
    private DefaultSensorMetricService service;

    @BeforeEach
    void setUp() {
        this.sensorMeasurementRepository = mock(SensorMeasurementRepository.class);
        this.service = new DefaultSensorMetricService(this.sensorMeasurementRepository);
    }

    @Test
    void shouldReturn30DaysMetricsMetricsForExistentMeasurements() {
        String sensorId = "123";
        int days = 30;

        givenMeasureListForSensor(sensorId);

        var sensorMetrics = this.service.getLast30DaysMetrics("123");

        assertNotNull(sensorMetrics);
        assertThatCorrectSensorIdAndStartDateWereSent(sensorId, days);
        assertThatMetricsAreCalculatedCorrectly(sensorMetrics);
    }

    private void assertThatCorrectSensorIdAndStartDateWereSent(String sensorId, int days) {
        var now = OffsetDateTime.now().withNano(0);
        var sensorIdArgumentCaptor = ArgumentCaptor.forClass(String.class);
        var startDateArgumentCaptor = ArgumentCaptor.forClass(OffsetDateTime.class);

        verify(this.sensorMeasurementRepository)
                .findAllMeasurementBySensorIdAndTimestampAfter(
                        sensorIdArgumentCaptor.capture(),
                        startDateArgumentCaptor.capture());

        assertEquals(now.minus(days, ChronoUnit.DAYS), startDateArgumentCaptor.getValue());
        assertEquals(sensorId, sensorIdArgumentCaptor.getValue());
    }

    private void givenMeasureListForSensor(String sensorId) {
        when(this.sensorMeasurementRepository
                .findAllMeasurementBySensorIdAndTimestampAfter(eq(sensorId), any(OffsetDateTime.class)))
                .thenReturn(mockedMeasurementList(sensorId));
    }

    private void assertThatMetricsAreCalculatedCorrectly(SensorMetric sensorMetrics) {
        assertEquals(10, sensorMetrics.getMax());
        assertEquals(5.5, sensorMetrics.getAverage());
    }

    private List<SensorMeasurement> mockedMeasurementList(String sensorId) {
        var sensor = new Sensor(sensorId);
        return IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new SensorMeasurement(sensor, i, OffsetDateTime.now()))
                .collect(Collectors.toList());
    }
}