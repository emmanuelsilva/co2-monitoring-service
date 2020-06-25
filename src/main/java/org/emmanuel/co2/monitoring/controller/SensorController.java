package org.emmanuel.co2.monitoring.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
import org.emmanuel.co2.monitoring.service.SensorAlertService;
import org.emmanuel.co2.monitoring.service.SensorMeasurementService;
import org.emmanuel.co2.monitoring.service.SensorMetricService;
import org.emmanuel.co2.monitoring.service.SensorStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@Slf4j
@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorMeasurementService sensorMeasurementService;
    private final SensorMetricService sensorMetricService;
    private final SensorStatusService sensorStatusService;
    private final SensorAlertService sensorAlertService;

    @PostMapping("/{sensorId}/mesurements")
    ResponseEntity<?> sensorMeasurementApi(@PathVariable("sensorId") String sensorId,
                                           @RequestBody Co2MeasurementRequest request) {

        log.info("receiving new measurement request for sensor {} and body {}", sensorId, request);

        SensorMeasurementRequest sensorMeasurementRequest = SensorMeasurementRequest.builder()
                .sensorId(sensorId)
                .time(request.getTime())
                .value(request.getCo2())
                .build();

        sensorMeasurementService.measure(sensorMeasurementRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sensorId}/metrics")
    ResponseEntity<SensorMetricResponse> sensorMetricsApi(@PathVariable("sensorId") String sensorId) {
        log.info("Request metrics for sensor {}", sensorId);

        var metrics = sensorMetricService.getLast30DaysMetrics(sensorId);
        var response = new SensorMetricResponse(metrics.getMax(), metrics.getAverage());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sensorId}")
    ResponseEntity<?> getSensorStatus(@PathVariable("sensorId") String sensorId) {
        var state = sensorStatusService.getCurrentState(sensorId);
        var response = new SensorStatusResponse(state);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sensorId}/alerts")
    ResponseEntity<?> getAlerts(@PathVariable("sensorId") String sensorId) {
        var alerts = this.sensorAlertService.getAlerts(sensorId);
        var response = alerts
        .stream()
        .map(a -> {
            var higherReads = a.getHigherReads();

            return SensorAlertResponse.builder()
                    .startTime(a.getStartAt().withNano(0))
                    .endTime(a.getEndAt())
                    .measurement1(higherReads.get(0))
                    .measurement2(higherReads.get(1))
                    .measurement3(higherReads.get(2))
                    .build();
        });

        return ResponseEntity.ok(response);
    }

    @Data
    private static class Co2MeasurementRequest {
        private int co2;
        private OffsetDateTime time;
    }

    @Data
    @Builder
    private static class SensorMetricResponse {
        private int maxLast30Days;
        private double avgLast30Days;
    }

    @Data
    @Builder
    private static class SensorStatusResponse {
        private SensorState status;
    }

    @Data
    @Builder
    private static class SensorAlertResponse {

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd'T'hh:mm:ssX")
        private OffsetDateTime startTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd'T'hh:mm:ssX")
        private OffsetDateTime endTime;
        private int measurement1;
        private int measurement2;
        private int measurement3;
    }
}
