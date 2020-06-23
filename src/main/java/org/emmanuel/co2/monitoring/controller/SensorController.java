package org.emmanuel.co2.monitoring.controller;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.emmanuel.co2.monitoring.domain.entity.SensorMetric;
import org.emmanuel.co2.monitoring.domain.entity.SensorState;
import org.emmanuel.co2.monitoring.dto.SensorMeasurementRequest;
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

}
