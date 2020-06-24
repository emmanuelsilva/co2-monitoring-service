package org.emmanuel.co2.monitoring.service;

import org.emmanuel.co2.monitoring.domain.entity.Sensor;
import org.emmanuel.co2.monitoring.domain.entity.SensorAlert;
import org.emmanuel.co2.monitoring.domain.repository.SensorAlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultSensorAlertServiceTest {

    private SensorAlertRepository repository;
    private DefaultSensorAlertService service;

    @BeforeEach
    void setUp() {
        this.repository = mock(SensorAlertRepository.class);
        this.service = new DefaultSensorAlertService(this.repository);
    }

    @Test
    void getAlerts() {
        var sensor = new Sensor("123");
        var quantity = 50;
        givenAlertsForSensorOnDatabase(sensor, quantity);

        var alerts = this.service.getAlerts(sensor.getId());

        assertEquals(quantity, alerts.size());
    }

    private void givenAlertsForSensorOnDatabase(Sensor sensor, int quantity) {
        when(this.repository.findAllBySensorId(sensor.getId()))
                .thenReturn(mockAlertList(sensor, quantity));
    }

    private List<SensorAlert> mockAlertList(Sensor sensor, int quantity) {
        IntFunction<SensorAlert> mapper = (i) -> {
            var now = OffsetDateTime.now();
            return SensorAlert.create(sensor, now, now);
        };

        return IntStream.rangeClosed(1, quantity)
                .mapToObj(mapper)
                .collect(Collectors.toList());
    }
}